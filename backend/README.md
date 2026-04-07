# Frontend Setup and Running

This project uses **Java + SpringBoot + Postgres**.

---

Before running the project, make sure you have installed:

- **docker**
- **docker compose**

```bash
docker --version
docker compose --version
```

To develop the application, make sure you have installed:

- **mvn**
- **jdk** (JDK 25 LTS)

```bash
mvn --version
java --version
```

---

## Verifying the application

Navigate to the backend directory:

```bash
cd backend
```

Check that the application builds fine:

```bash
mvn verify
```

---

## Run the Application

Move to the top folder:

```bash
# If inside backend folder
cd ..

# While in the folder with compose.yaml (for the frontend + backend)
docker compose -f compose.yaml up -d --build
# For just the backend without the frontend
docker compose -f compose.backend.dev.yaml up -d --build
```

The API will be available at:

```
http://localhost:8080/
```

## Notes

---