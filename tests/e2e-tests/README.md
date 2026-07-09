# Asset Booking/Management
### End-To-End test suite

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
| `mvn test -Dtest=**/FolderName/**` | Run all tests from a specific folder|
| `mvn test -Dtest=FileName` | Run all tests from a specific file |
| `mvn test -Dtest=FileName#testName` | Run a single test |
| `mvn test -Dbrowser=firefox` | Run tests in Firefox (default is Chrome) |
| `mvn test -Dsleep=1000` | Set delay between tests (default is 5000ms) |

**Examples:**
```
mvn test -Dtest=**/auth/**
mvn test -Dtest=LoginTest
mvn test -Dtest=LoginTest#UserCanLogin
```

---

## IntelliJ IDEA

1. Open the desired test file and right-click → **Run**
2. To run all tests:
   - Open the `java` folder
   - Right-click → **Run**

---

# Running API Tests (REST Assured)

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
| `mvn test -Dtest=**/api/**` | Run all API tests |
| `mvn test -Dtest=FileName` | Run all tests from a specific file |
| `mvn test -Dtest=FileName#testName` | Run a single test |
| `mvn test -Denv=staging` | Run tests against a specific environment (default is local) |

**Examples:**
```
mvn test -Dtest=**/api/booking/**
mvn test -Dtest=AssetApiTest
mvn test -Dtest=AssetApiTest#createAssetReturns201
```

---

## IntelliJ IDEA

1. Open the desired test file and right-click → **Run**
2. To run all API tests:
   - Open the `api` folder (inside the `java` folder)
   - Right-click → **Run**

---

# Running Load/Performance Tests (Gatling)

## Prerequisites
- Application must be running before executing tests
- Make sure the target URL/number of users is configured in the simulation script (e.g. `baseUrl`, `users`, `rampDuration`)

---

## Visual Studio Code

1. Open a new terminal and navigate to the load tests folder:
   ```
   cd asset-booking-management/tests/load-tests
   ```
2. Run the tests using one of the following commands:

| Command                                                                      | Description |
|------------------------------------------------------------------------------|---|
| `mvn gatling:test`                                                           | Run all simulations |
| `mvn gatling:test -Dgatling.simulationClass=simulations.SimulationClassName` | Run a specific simulation |

**Examples:**
```
mvn gatling:test -Dgatling.simulationClass=simulations.BookingLoadSimulation
```

3. After execution, Gatling prints a link to the HTML report in the console. Copy that link and open it in a browser to view the results (response times, throughput, error rate).

---

## IntelliJ IDEA

1. Open the simulation class (e.g. `BookingLoadSimulation.scala`/`.java`) → right-click → **Run**
