#!/usr/bin/env pwsh
<#
  Test Script for AutoVulcan Core Engine
  Tests the analyzer service with Petshop repository
#>

Write-Host "╔════════════════════════════════════════════════════════════════╗"
Write-Host "║     AutoVulcan Core - Engine Test with Petshop Repository    ║"
Write-Host "╚════════════════════════════════════════════════════════════════╝`n"

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$resultsDir = Join-Path $projectRoot "analysis-results"

# Create results directory
if (-not (Test-Path $resultsDir)) {
    New-Item -ItemType Directory -Path $resultsDir | Out-Null
    Write-Host "📁 Created results directory: $resultsDir`n"
}

# Step 1: Start service
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
Write-Host "Step 1: Starting Analyzer Service"
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Kill existing Java processes
Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force
Start-Sleep -Seconds 2

# Start service
Write-Host "🚀 Starting: java -Xmx2g -jar target/analyzer-service-1.0.0.jar`n"
$serviceJob = Start-Job -WorkingDirectory $projectRoot -ScriptBlock {
    java -Xmx2g -jar target/analyzer-service-1.0.0.jar
} -Name "AnalyzerService"

Write-Host "⏳ Waiting for service to initialize (30 seconds)..."
Start-Sleep -Seconds 30

# Check if service is running
$running = $false
for ($i = 0; $i -lt 5; $i++) {
    try {
        $health = Invoke-WebRequest -Uri "http://localhost:8080/api/health" -TimeoutSec 3 -ErrorAction SilentlyContinue
        Write-Host "✅ Service is ONLINE and responding!`n"
        $running = $true
        break
    } catch {
        Start-Sleep -Seconds 2
    }
}

if (-not $running) {
    Write-Host "❌ Service failed to start`n"
    Get-Job -Name "AnalyzerService" | Receive-Job | Select-Object -Last 20
    exit 1
}

# Step 2: Send analysis request
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
Write-Host "Step 2: Sending Analysis Request"
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

$payload = @{
    sourceZipUrl = "https://github.com/Stewella/Petshop/archive/refs/heads/main.zip"
    projectId = ""
    dependencies = @()
} | ConvertTo-Json

Write-Host "📤 Request Details:"
Write-Host "  URL: http://localhost:8080/api/analyze"
Write-Host "  Method: POST"
Write-Host "  Repository: Petshop (github.com/Stewella/Petshop)`n"

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/analyze" `
        -Method POST `
        -ContentType "application/json" `
        -Body $payload `
        -TimeoutSec 60 `
        -ErrorAction Stop
    
    $result = $response.Content | ConvertFrom-Json
    $projectId = $result.projectId
    
    Write-Host "✅ Request accepted!"
    Write-Host "  Status: $($result.status)"
    Write-Host "  ProjectId: $projectId"
    Write-Host "  Message: $($result.message)`n"
    
    # Save initial response
    $responseFile = Join-Path $resultsDir "initial-response.json"
    $result | ConvertTo-Json | Out-File -FilePath $responseFile -Encoding UTF8
    Write-Host "  Saved to: $responseFile`n"
    
} catch {
    Write-Host "❌ Request failed: $($_.Exception.Message)`n"
    exit 1
}

# Step 3: Wait for analysis and monitor logs
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
Write-Host "Step 3: Waiting for Analysis to Complete"
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

$maxWait = 180  # 3 minutes
$elapsed = 0
$checkInterval = 10

Write-Host "⏳ Analysis in progress... (max wait: $maxWait seconds)`n"

while ($elapsed -lt $maxWait) {
    Write-Host -NoNewline "`r⏳ Elapsed: $elapsed/$maxWait seconds  "
    Start-Sleep -Seconds $checkInterval
    $elapsed += $checkInterval
    
    # Check if job has any errors
    $jobState = Get-Job -Name "AnalyzerService" | Select-Object -ExpandProperty State
    if ($jobState -ne "Running") {
        Write-Host "`n⚠️ Service job state changed to: $jobState"
        break
    }
}

Write-Host "`n✓ Analysis completed or timeout reached`n"

# Step 4: Check repository for results
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
Write-Host "Step 4: Checking for Generated Results"
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

$appRepoBase = "/app/repository"
if ($IsWindows) {
    # On Windows, check local repository
    $appRepoBase = Join-Path $projectRoot "repository"
}

Write-Host "Looking for results in: $appRepoBase`n"

if (Test-Path $appRepoBase) {
    Get-ChildItem -Path $appRepoBase -Recurse -Force | ForEach-Object {
        $relPath = $_.FullName -replace [regex]::Escape($appRepoBase), ""
        Write-Host "  $relPath"
    }
    
    # Copy repository contents to results
    Copy-Item -Path $appRepoBase -Destination (Join-Path $resultsDir "repository") -Recurse -Force
    Write-Host "`n✓ Repository contents copied to $resultsDir`n"
} else {
    Write-Host "⚠️ Repository directory not found at $appRepoBase`n"
}

# Step 5: Summary
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
Write-Host "Step 5: Test Summary"
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

Write-Host "📁 Results Location: $resultsDir`n"

if (Test-Path $resultsDir) {
    $files = Get-ChildItem -Path $resultsDir -File -Recurse
    Write-Host "📋 Generated Files ($($files.Count) total):"
    $files | ForEach-Object {
        $size = if ($_.Length -gt 1MB) { 
            "{0:F2} MB" -f ($_.Length / 1MB) 
        } elseif ($_.Length -gt 1KB) { 
            "{0:F2} KB" -f ($_.Length / 1KB) 
        } else { 
            "$($_.Length) B" 
        }
        Write-Host "  - $($_.Name) [$size]"
    }
    Write-Host ""
}

# Check service logs
Write-Host "📝 Recent Service Output:"
Write-Host "─────────────────────────────────────────"
Get-Job -Name "AnalyzerService" | Receive-Job | Select-Object -Last 30 | ForEach-Object {
    Write-Host $_
}

Write-Host "`n✅ Test completed! Check $resultsDir for results.`n"
