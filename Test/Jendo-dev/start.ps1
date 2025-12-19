# Jendo App Quick Start Script
# This script helps you quickly start both frontend and backend

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Jendo App - Quick Start Script" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Define paths
$backendPath = "c:\Users\HP\OneDrive\Desktop\jendo_project\Jendo-Backend\Jendo-Backend"
$frontendPath = "c:\Users\HP\OneDrive\Desktop\jendo_project\Expo (3)\Expo"

# Function to check if a port is in use
function Test-Port {
    param($Port)
    $connection = Test-NetConnection -ComputerName localhost -Port $Port -WarningAction SilentlyContinue
    return $connection.TcpTestSucceeded
}

# Check prerequisites
Write-Host "Checking prerequisites..." -ForegroundColor Yellow
Write-Host ""

# Check Java
try {
    $javaVersion = java -version 2>&1 | Select-String "version"
    Write-Host "✓ Java found: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "✗ Java not found. Please install Java 17 or higher." -ForegroundColor Red
    exit 1
}

# Check Maven
try {
    $mavenVersion = mvn -version 2>&1 | Select-String "Apache Maven"
    Write-Host "✓ Maven found: $mavenVersion" -ForegroundColor Green
} catch {
    Write-Host "✗ Maven not found. Please install Maven." -ForegroundColor Red
    exit 1
}

# Check Node.js
try {
    $nodeVersion = node --version
    Write-Host "✓ Node.js found: $nodeVersion" -ForegroundColor Green
} catch {
    Write-Host "✗ Node.js not found. Please install Node.js." -ForegroundColor Red
    exit 1
}

# Check npm
try {
    $npmVersion = npm --version
    Write-Host "✓ npm found: v$npmVersion" -ForegroundColor Green
} catch {
    Write-Host "✗ npm not found. Please install npm." -ForegroundColor Red
    exit 1
}

Write-Host ""

# Check if PostgreSQL is running (port 5432)
Write-Host "Checking PostgreSQL..." -ForegroundColor Yellow
if (Test-Port -Port 5432) {
    Write-Host "✓ PostgreSQL is running on port 5432" -ForegroundColor Green
} else {
    Write-Host "✗ PostgreSQL is not running on port 5432" -ForegroundColor Red
    Write-Host "  Please start PostgreSQL before continuing." -ForegroundColor Yellow
    $continue = Read-Host "Continue anyway? (y/n)"
    if ($continue -ne 'y') {
        exit 1
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "What would you like to do?" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "1. Start Backend only" -ForegroundColor White
Write-Host "2. Start Frontend only" -ForegroundColor White
Write-Host "3. Start Both (in separate windows)" -ForegroundColor White
Write-Host "4. Test Backend connection" -ForegroundColor White
Write-Host "5. Install Frontend dependencies" -ForegroundColor White
Write-Host "6. Build Backend" -ForegroundColor White
Write-Host "7. Exit" -ForegroundColor White
Write-Host ""

$choice = Read-Host "Enter your choice (1-7)"

switch ($choice) {
    "1" {
        Write-Host ""
        Write-Host "Starting Backend..." -ForegroundColor Yellow
        Write-Host "Backend will run on http://localhost:5000" -ForegroundColor Cyan
        Write-Host "Swagger UI: http://localhost:5000/swagger-ui.html" -ForegroundColor Cyan
        Write-Host ""
        
        Set-Location $backendPath
        mvn spring-boot:run
    }
    
    "2" {
        Write-Host ""
        Write-Host "Starting Frontend..." -ForegroundColor Yellow
        Write-Host "Metro Bundler will run on http://localhost:8081" -ForegroundColor Cyan
        Write-Host ""
        
        Set-Location $frontendPath
        npm start
    }
    
    "3" {
        Write-Host ""
        Write-Host "Starting Backend in new window..." -ForegroundColor Yellow
        Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$backendPath'; Write-Host 'Starting Backend...' -ForegroundColor Cyan; Write-Host 'Backend URL: http://localhost:5000' -ForegroundColor Green; Write-Host 'Swagger UI: http://localhost:5000/swagger-ui.html' -ForegroundColor Green; Write-Host ''; mvn spring-boot:run"
        
        Write-Host "Waiting 5 seconds for backend to initialize..." -ForegroundColor Yellow
        Start-Sleep -Seconds 5
        
        Write-Host "Starting Frontend in new window..." -ForegroundColor Yellow
        Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$frontendPath'; Write-Host 'Starting Frontend...' -ForegroundColor Cyan; Write-Host 'Make sure backend is running first!' -ForegroundColor Yellow; Write-Host ''; npm start"
        
        Write-Host ""
        Write-Host "✓ Both applications starting in separate windows!" -ForegroundColor Green
        Write-Host ""
        Write-Host "Backend: http://localhost:5000" -ForegroundColor Cyan
        Write-Host "Swagger: http://localhost:5000/swagger-ui.html" -ForegroundColor Cyan
        Write-Host "Frontend: Scan QR code or press w/a/i in Metro Bundler" -ForegroundColor Cyan
    }
    
    "4" {
        Write-Host ""
        Write-Host "Testing Backend connection..." -ForegroundColor Yellow
        
        if (Test-Port -Port 5000) {
            Write-Host "✓ Backend is running on port 5000" -ForegroundColor Green
            
            try {
                $response = Invoke-WebRequest -Uri "http://localhost:5000/swagger-ui.html" -Method GET -TimeoutSec 5
                Write-Host "✓ Backend is responding" -ForegroundColor Green
                Write-Host "  Swagger UI is accessible at http://localhost:5000/swagger-ui.html" -ForegroundColor Cyan
            } catch {
                Write-Host "✗ Backend is not responding" -ForegroundColor Red
                Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor Red
            }
        } else {
            Write-Host "✗ Backend is not running on port 5000" -ForegroundColor Red
            Write-Host "  Please start the backend first." -ForegroundColor Yellow
        }
    }
    
    "5" {
        Write-Host ""
        Write-Host "Installing Frontend dependencies..." -ForegroundColor Yellow
        
        Set-Location $frontendPath
        npm install
        
        Write-Host ""
        Write-Host "✓ Dependencies installed!" -ForegroundColor Green
    }
    
    "6" {
        Write-Host ""
        Write-Host "Building Backend..." -ForegroundColor Yellow
        
        Set-Location $backendPath
        mvn clean install
        
        Write-Host ""
        Write-Host "✓ Backend built successfully!" -ForegroundColor Green
    }
    
    "7" {
        Write-Host ""
        Write-Host "Goodbye!" -ForegroundColor Cyan
        exit 0
    }
    
    default {
        Write-Host ""
        Write-Host "Invalid choice. Please run the script again." -ForegroundColor Red
        exit 1
    }
}
