#!/bin/bash

# PROJECTEND - SPRING BOOT APPLICATION
# Script chạy ứng dụng Spring Boot với auto-kill port cũ

echo "🚀 === PROJECTEND SPRING BOOT APPLICATION ==="
echo "📅 Starting at $(date)"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

# Check if running on macOS or Linux
if [[ "$OSTYPE" == "darwin"* ]]; then
    OS="macOS"
elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
    OS="Linux"
else
    OS="Unknown"
fi

print_info "Detected OS: $OS"

# Kill existing process on port 8080
print_info "Checking for existing processes on port 8080..."

if [[ "$OS" == "macOS" ]]; then
    # macOS version
    EXISTING_PID=$(lsof -ti:8080)
    if [ ! -z "$EXISTING_PID" ]; then
        print_warning "Found existing process on port 8080 (PID: $EXISTING_PID)"
        print_info "Killing existing process..."
        kill -9 $EXISTING_PID
        sleep 2
        print_status "Successfully killed existing process"
    else
        print_status "Port 8080 is available"
    fi
else
    # Linux version
    EXISTING_PID=$(fuser -n tcp 8080 2>/dev/null | awk '{print $1}')
    if [ ! -z "$EXISTING_PID" ]; then
        print_warning "Found existing process on port 8080 (PID: $EXISTING_PID)"
        print_info "Killing existing process..."
        kill -9 $EXISTING_PID
        sleep 2
        print_status "Successfully killed existing process"
    else
        print_status "Port 8080 is available"
    fi
fi

# Check Java installation
print_info "Checking Java installation..."
if command -v java >/dev/null 2>&1; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)
    print_status "Java is installed: $JAVA_VERSION"

    # Check if Java version is 17 or higher
    JAVA_MAJOR_VERSION=$(echo $JAVA_VERSION | cut -d'.' -f1)
    if [ "$JAVA_MAJOR_VERSION" -ge 17 ] 2>/dev/null; then
        print_status "Java version is compatible (17+)"
    else
        print_warning "Java version might be too old. Recommended: Java 17+"
    fi
else
    print_error "Java is not installed or not in PATH!"
    print_info "Please install Java 17+ from: https://adoptium.net/"
    exit 1
fi

# Check Maven installation
print_info "Checking Maven installation..."
if command -v mvn >/dev/null 2>&1; then
    MAVEN_VERSION=$(mvn -version 2>/dev/null | head -n 1 | cut -d' ' -f3)
    print_status "Maven is installed: $MAVEN_VERSION"
else
    print_error "Maven is not installed or not in PATH!"
    print_info "Please install Maven from: https://maven.apache.org/install.html"
    print_info "Or use Maven Wrapper: ./mvnw instead of mvn"
    exit 1
fi

# Check if pom.xml exists
if [ ! -f "pom.xml" ]; then
    print_error "pom.xml not found! Make sure you're in the project root directory."
    exit 1
fi

print_status "All prerequisites are satisfied"

echo ""
print_info "=== STEP 1: CLEANING PREVIOUS BUILD ==="
mvn clean -q

if [ $? -ne 0 ]; then
    print_error "Maven clean failed!"
    exit 1
fi
print_status "Clean completed successfully"

echo ""
print_info "=== STEP 2: COMPILING PROJECT ==="
mvn compile -q

if [ $? -ne 0 ]; then
    print_error "Compilation failed! Please check your code for errors."
    exit 1
fi
print_status "Compilation completed successfully"

echo ""
print_info "=== STEP 3: STARTING SPRING BOOT APPLICATION ==="
print_info "🌐 Application will be available at: http://localhost:8080"
print_info "📱 Main pages:"
print_info "   • Home: http://localhost:8080/"
print_info "   • Cart: http://localhost:8080/giohang"
print_info "   • Products: http://localhost:8080/sanpham"
print_info "   • Login: http://localhost:8080/login"
echo ""
print_warning "Press Ctrl+C to stop the application"
echo ""

# Function to check if Spring Boot is ready
wait_for_spring_boot() {
    print_info "⏳ Waiting for Spring Boot to start..."
    local max_attempts=30
    local attempt=1

    while [ $attempt -le $max_attempts ]; do
        if curl -s http://localhost:8080 > /dev/null 2>&1; then
            print_status "Spring Boot is ready!"
            return 0
        fi
        sleep 2
        attempt=$((attempt + 1))
    done

    print_warning "Spring Boot took longer than expected to start"
    return 1
}

# Function to open browser
open_browser() {
    if command -v open >/dev/null 2>&1; then
        # macOS
        print_info "🌐 Opening browser..."
        open http://localhost:8080
    elif command -v xdg-open >/dev/null 2>&1; then
        # Linux
        print_info "🌐 Opening browser..."
        xdg-open http://localhost:8080
    elif command -v start >/dev/null 2>&1; then
        # Windows
        print_info "🌐 Opening browser..."
        start http://localhost:8080
    else
        print_info "🌐 Please open your browser and go to: http://localhost:8080"
    fi
}

# Start Spring Boot in background
print_info "🚀 Starting Spring Boot application..."
mvn spring-boot:run &
SPRING_PID=$!

# Wait for Spring Boot to be ready, then open browser
if wait_for_spring_boot; then
    open_browser
fi

# Wait for the Spring Boot process
wait $SPRING_PID

# This will run when Ctrl+C is pressed
echo ""
print_info "Application stopped."
print_info "Goodbye! 👋"
