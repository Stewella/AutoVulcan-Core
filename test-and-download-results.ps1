#!/usr/bin/env pwsh
<#
.SYNOPSIS
    Test AutoVulcan Core with Petshop repository and download JSON results
.DESCRIPTION
    This script will:
    1. Start the analyzer service
    2. Send analysis request for Petshop repository
    3. Wait for analysis to complete
    4. Copy JSON results to local 'analysis-results' folder
#>

param(
    [string]$RepositoryUrl = "https://github.com/Stewella/Petshop/archive/refs/heads/main.zip",
    [int]$WaitTimeSeconds = 180
)

$ErrorActionPreference = "Continue"
$projectRoot = "d:\STEFFANY H\STE_\KULIAH MD\Proyek Penelitian Terapan\autovulcan_core"
$localResultsDir = Join-Path $projectRoot "analysis-results"
$timestamp = Get-Date -Format "yyyyMMdd_HHmmss"

Write-Host "╔═══════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║   AutoVulcan Core - Test & Download Results                 ║" -ForegroundColor Cyan
Write-Host "╚═══════════════════════════════════════════════════════════════╝`n" -ForegroundColor Cyan

# Create local results directory
if (-not (Test-Path $localResultsDir)) {
    New-Item -ItemType Directory -Path $localResultsDir | Out-Null
}
Write-Host "📁 Local results directory: $localResultsDir`n" -ForegroundColor Yellow

# Step 1: Clean up any existing Java processes
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Gray
Write-Host "Step 1: Cleanup existing processes" -ForegroundColor White
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━`n" -ForegroundColor Gray

Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force -ErrorAction SilentlyContinue
Get-Job | Remove-Job -Force -ErrorAction SilentlyContinue
Start-Sleep -Seconds 2
Write-Host "✓ Cleaned up existing processes`n" -ForegroundColor Green

# Step 2: Start the service
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Gray
Write-Host "Step 2: Starting Analyzer Service" -ForegroundColor White
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━`n" -ForegroundColor Gray

Set-Location $projectRoot

# Check if JAR exists
if (-not (Test-Path "target\analyzer-service-1.0.0.jar")) {
    Write-Host "❌ JAR file not found! Please run 'mvn clean package -DskipTests' first" -ForegroundColor Red
    exit 1
}

Write-Host "Starting service in background..." -ForegroundColor Yellow
$serviceJob = Start-Job -ScriptBlock {
    Set-Location "d:\STEFFANY H\STE_\KULIAH MD\Proyek Penelitian Terapan\autovulcan_core"
    java -Xmx2g -jar target/analyzer-service-1.0.0.jar 2>&1
} -Name "AutoVulcanService"

Write-Host "Job ID: $($serviceJob.Id)" -ForegroundColor Gray
Write-Host "Waiting 30 seconds for service to initialize..." -ForegroundColor Yellow

for ($i = 30; $i -gt 0; $i--) {
    Write-Host -NoNewline "`r⏳ $i seconds remaining...  "
    Start-Sleep -Seconds 1
}
Write-Host "`n"

# Verify service is running
$serviceReady = $false
for ($attempt = 1; $attempt -le 5; $attempt++) {
    try {
        $health = Invoke-WebRequest -Uri "http://localhost:8080/api/health" -TimeoutSec 5 -ErrorAction Stop
        Write-Host "✅ Service is ONLINE and responding!" -ForegroundColor Green
        Write-Host "   Health check: $($health.StatusCode)`n" -ForegroundColor Gray
        $serviceReady = $true
        break
    } catch {
        Write-Host "⏳ Attempt $attempt/5 - Service not ready yet..." -ForegroundColor Yellow
        Start-Sleep -Seconds 3
    }
}

if (-not $serviceReady) {
    Write-Host "❌ Service failed to start properly`n" -ForegroundColor Red
    Write-Host "Service logs:" -ForegroundColor Yellow
    Get-Job -Name "AutoVulcanService" | Receive-Job | Select-Object -Last 20
    exit 1
}

# Step 3: Send analysis request
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Gray
Write-Host "Step 3: Sending Analysis Request" -ForegroundColor White
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━`n" -ForegroundColor Gray

$payload = @{
    sourceZipUrl = $RepositoryUrl
    projectId = ""
    dependencies = @()
} | ConvertTo-Json

Write-Host "📤 Request Details:" -ForegroundColor Yellow
Write-Host "   URL: http://localhost:8080/api/analyze" -ForegroundColor Gray
Write-Host "   Repository: $RepositoryUrl" -ForegroundColor Gray
Write-Host "   Method: POST`n" -ForegroundColor Gray

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/analyze" `
        -Method POST `
        -ContentType "application/json" `
        -Body $payload `
        -TimeoutSec 60 `
        -ErrorAction Stop
    
    $result = $response.Content | ConvertFrom-Json
    $projectId = $result.projectId
    
    Write-Host "✅ Request ACCEPTED!" -ForegroundColor Green
    Write-Host "   Status: $($result.status)" -ForegroundColor Gray
    Write-Host "   ProjectId: $projectId" -ForegroundColor Cyan
    Write-Host "   Message: $($result.message)`n" -ForegroundColor Gray
    
    # Save initial response
    $initialResponseFile = Join-Path $localResultsDir "request-response-$timestamp.json"
    $result | ConvertTo-Json | Out-File -FilePath $initialResponseFile -Encoding UTF8
    Write-Host "💾 Initial response saved to: $initialResponseFile`n" -ForegroundColor Green
    
} catch {
    Write-Host "❌ Request FAILED: $($_.Exception.Message)`n" -ForegroundColor Red
    Get-Job -Name "AutoVulcanService" | Stop-Job
    Get-Job -Name "AutoVulcanService" | Remove-Job
    exit 1
}

# Step 4: Wait for analysis to complete
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Gray
Write-Host "Step 4: Waiting for Analysis to Complete" -ForegroundColor White
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━`n" -ForegroundColor Gray

Write-Host "⏳ Analysis in progress (max wait: $WaitTimeSeconds seconds)..." -ForegroundColor Yellow
Write-Host "   This includes: download → compile → analyze → generate results`n" -ForegroundColor Gray

$checkInterval = 10
$elapsed = 0

while ($elapsed -lt $WaitTimeSeconds) {
    $percent = [math]::Round(($elapsed / $WaitTimeSeconds) * 100)
    Write-Host -NoNewline "`r⏳ Progress: $elapsed/$WaitTimeSeconds sec [$percent%]  "
    
    # Check if results file exists
    $resultsPath = Join-Path $projectRoot "repository\$projectId\results\analysis-result.json"
    if (Test-Path $resultsPath) {
        Write-Host "`n✅ Analysis completed! Results found.`n" -ForegroundColor Green
        break
    }
    
    Start-Sleep -Seconds $checkInterval
    $elapsed += $checkInterval
}

Write-Host "`n"

# Step 5: Download and copy results
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Gray
Write-Host "Step 5: Downloading Results to Local Directory" -ForegroundColor White
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━`n" -ForegroundColor Gray

$repositoryBase = Join-Path $projectRoot "repository"
$projectDir = Join-Path $repositoryBase $projectId

Write-Host "📂 Looking for results in: $projectDir`n" -ForegroundColor Yellow

if (Test-Path $projectDir) {
    # Create local project results folder
    $localProjectDir = Join-Path $localResultsDir "petshop-$timestamp"
    New-Item -ItemType Directory -Path $localProjectDir -Force | Out-Null
    
    # Copy the entire project directory
    Write-Host "📥 Copying analysis results..." -ForegroundColor Yellow
    Copy-Item -Path $projectDir -Destination $localProjectDir -Recurse -Force
    Write-Host "✅ Project directory copied to: $localProjectDir`n" -ForegroundColor Green
    
    # Check for JSON result file
    $jsonResultPath = Join-Path $projectDir "results\analysis-result.json"
    if (Test-Path $jsonResultPath) {
        # Copy JSON to root of local results with descriptive name
        $localJsonPath = Join-Path $localResultsDir "petshop-analysis-$timestamp.json"
        Copy-Item -Path $jsonResultPath -Destination $localJsonPath -Force
        
        Write-Host "🎉 SUCCESS! JSON result file found and copied!" -ForegroundColor Green
        Write-Host "   Source: $jsonResultPath" -ForegroundColor Gray
        Write-Host "   Destination: $localJsonPath`n" -ForegroundColor Cyan
        
        # Display JSON summary
        $jsonContent = Get-Content $localJsonPath -Raw | ConvertFrom-Json
        
        Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Gray
        Write-Host "📊 Analysis Result Summary" -ForegroundColor White
        Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Gray
        Write-Host "   Project ID: $($jsonContent.projectId)" -ForegroundColor Cyan
        
        if ($jsonContent.callGraph) {
            $nodeCount = if ($jsonContent.callGraph.nodes) { $jsonContent.callGraph.nodes.Count } else { 0 }
            $edgeCount = if ($jsonContent.callGraph.edges) { $jsonContent.callGraph.edges.Count } else { 0 }
            Write-Host "   Call Graph Nodes: $nodeCount" -ForegroundColor Gray
            Write-Host "   Call Graph Edges: $edgeCount" -ForegroundColor Gray
        }
        
        if ($jsonContent.cfg) {
            Write-Host "   CFG Methods: $($jsonContent.cfg.Count)" -ForegroundColor Gray
        }
        
        Write-Host ""
        
        # Show file size
        $jsonFile = Get-Item $localJsonPath
        $sizeKB = [math]::Round($jsonFile.Length / 1KB, 2)
        Write-Host "   File Size: $sizeKB KB" -ForegroundColor Gray
        Write-Host ""
        
    } else {
        Write-Host "⚠️  JSON result file not found at: $jsonResultPath" -ForegroundColor Yellow
        Write-Host "   Analysis may still be in progress or failed.`n" -ForegroundColor Yellow
    }
    
    # List all files copied
    Write-Host "📋 Files downloaded:" -ForegroundColor Yellow
    Get-ChildItem -Path $localProjectDir -Recurse -File | ForEach-Object {
        $relativePath = $_.FullName.Replace($localProjectDir, "").TrimStart('\')
        $sizeStr = if ($_.Length -gt 1MB) { 
            "{0:F2} MB" -f ($_.Length / 1MB) 
        } elseif ($_.Length -gt 1KB) { 
            "{0:F2} KB" -f ($_.Length / 1KB) 
        } else { 
            "$($_.Length) B" 
        }
        Write-Host "   - $relativePath [$sizeStr]" -ForegroundColor Gray
    }
    Write-Host ""
    
} else {
    Write-Host "❌ Project directory not found: $projectDir" -ForegroundColor Red
    Write-Host "   Analysis may have failed. Check service logs below.`n" -ForegroundColor Yellow
}

# Step 6: Show service logs
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Gray
Write-Host "Step 6: Service Logs (last 50 lines)" -ForegroundColor White
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━`n" -ForegroundColor Gray

Get-Job -Name "AutoVulcanService" | Receive-Job | Select-Object -Last 50 | ForEach-Object {
    if ($_ -match "ERROR|error|failed|Failed") {
        Write-Host $_ -ForegroundColor Red
    } elseif ($_ -match "SUCCESS|completed|saved") {
        Write-Host $_ -ForegroundColor Green
    } else {
        Write-Host $_ -ForegroundColor Gray
    }
}

# Cleanup
Write-Host "`n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Gray
Write-Host "Cleanup" -ForegroundColor White
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━`n" -ForegroundColor Gray

Write-Host "Stopping service..." -ForegroundColor Yellow
Get-Job -Name "AutoVulcanService" | Stop-Job
Get-Job -Name "AutoVulcanService" | Remove-Job
Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force -ErrorAction SilentlyContinue

Write-Host "✓ Service stopped`n" -ForegroundColor Green

# Final summary
Write-Host "╔═══════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║                    TEST COMPLETED                            ║" -ForegroundColor Cyan
Write-Host "╚═══════════════════════════════════════════════════════════════╝`n" -ForegroundColor Cyan

Write-Host "📁 Your results are in: $localResultsDir" -ForegroundColor Cyan
Write-Host ""
Write-Host "To view results:" -ForegroundColor Yellow
Write-Host "   cd '$localResultsDir'" -ForegroundColor Gray
Write-Host "   Get-ChildItem" -ForegroundColor Gray
Write-Host ""
Write-Host "To view JSON in browser/editor:" -ForegroundColor Yellow
Write-Host "   code '$localResultsDir\petshop-analysis-$timestamp.json'" -ForegroundColor Gray
Write-Host ""
