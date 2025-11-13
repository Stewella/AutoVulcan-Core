# AutoVulcan
IF5200 Applied Research Project - Application KG For Development Vulnerability Mining

## 📘 Overview
The AutoVulcan-Core is the analytical and testing engine of the AutoVulcan framework.
It is designed to perform static code analysis, test case generation, and feature extraction from Java source or bytecode.
This module leverages:
- **SootUp** — for advanced static code analysis, building Call Graphs and Control Flow Graphs (CFGs).
- **EvoSuite** — for automated unit test generation and mutation testing.
- **PostgreSQL** — for storing extracted metadata and analysis results.
The resulting data (e.g., call graphs, control flow graphs, and test coverage metrics) are later consumed by backend and dashboard components for visualization and vulnerability reasoning.

## 🚀 Core Features
| Category | Description |
|----|:------:|
| Static Code Analysis | Analyze Java applications via SootUp, extracting call graphs, and CFGs |
| EvoSuite Integration | Automatically generate JUnit test cases for Java classes, and measure code coverage| 
|Data Persistence | Store analysis results in PostgreSQL for downstream modules |

## 🧩 Architecture Overview
          ┌──────────────────────────┐
          │  Java Source Repository  │
          └────────────┬─────────────┘
                       │
                (1) Compile
                       ▼
            ┌──────────────────┐
            │  Bytecode (.class)│
            └────────┬──────────┘
                     │
          (2) Static Analysis via SootUp
                     ▼
        ┌────────────────────────────┐
        │  AutoVulcan-Core Analyzer  │
        │  - Call Graph Builder      │
        │  - CFG Extractor           │
        └────────┬───────────────┬───┘
                 │               │
                 ▼               ▼
     (3) JSON / Graphviz      (4) EvoSuite
          Exporter                │
                                  ▼
                      ┌─────────────────────┐
                      │ Test Case Generator │
                      │  & Coverage Report  │
                      └────────┬────────────┘
                               │
                               ▼
                     (5) PostgreSQL Storage
