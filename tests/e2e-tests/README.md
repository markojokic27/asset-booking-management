# Asset Booking/Management

### End To End test suite

# Running E2E Tests

## Prerequisites
- Application must be running before executing tests

---

## Visual Studio Code

1. Open a new terminal and navigate to the E2E tests folder:
   ```
   cd asset-booking-management/tests/e2e-tests
   ```
2. Run the tests using one of the following commands:

| Command | Description |
|---|---|
| `mvn test` | Run all tests |
| `mvn test -Dtest=FileName` | Run all tests from a specific file |
| `mvn test -Dtest=FileName#testName` | Run a single test |
| `mvn test -Dbrowser=firefox` | Run tests in Firefox (default is Chrome) |
| `mvn test -Dsleep=1000` | Set delay between tests (default is 5000ms) |

**Examples:**
```
mvn test -Dtest=LoginTest
mvn test -Dtest=LoginTest#UserCanLogin
```

---

## IntelliJ IDEA

1. Open the desired test file and right-click → **Run**
2. To run all tests:
    - Open the `java` folder
    - Right-click → **Run**