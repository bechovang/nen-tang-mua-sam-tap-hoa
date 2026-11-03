$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $root

$envFile = Join-Path $root ".env.pilot"
if (-not (Test-Path $envFile)) {
    Write-Host "Thiếu .env.pilot. Copy từ .env.pilot.example rồi điền giá trị thật." -ForegroundColor Red
    exit 1
}

Get-Content $envFile | ForEach-Object {
    if ($_ -match '^\s*([A-Za-z0-9_]+)\s*=\s*(.*)$') {
        [Environment]::SetEnvironmentVariable($matches[1], $matches[2], "Process")
    }
}

$required = @("APP_BASE_URL", "DB_URL", "DB_USER", "DB_PASSWORD", "AUTH0_DOMAIN", "AUTH0_CLIENT_ID", "AUTH0_CLIENT_SECRET")
$missing = @()
foreach ($key in $required) {
    if ([string]::IsNullOrWhiteSpace([Environment]::GetEnvironmentVariable($key, "Process"))) {
        $missing += $key
    }
}
if ($missing.Count -gt 0) {
    Write-Host ("Thiếu biến môi trường: " + ($missing -join ", ")) -ForegroundColor Red
    exit 1
}

$jar = Join-Path $root "target\taphoagan-0.0.1-SNAPSHOT.jar"
if (-not (Test-Path $jar)) {
    mvn -q -DskipTests package
}

Write-Host "Pilot đang chạy tại $env:APP_BASE_URL" -ForegroundColor Green
Write-Host "Auth0 callback: $env:APP_BASE_URL/login/oauth2/code/auth0" -ForegroundColor Yellow
java -jar $jar --spring.profiles.active=pilot
