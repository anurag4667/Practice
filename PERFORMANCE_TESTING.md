# Performance Testing

This project includes a Gatling performance simulation for your API endpoints:

- `POST /user`
- `GET /user/{id}`
- `GET /user`

Simulation class:

- `src/test/java/org/pm/smarty/performance/UserApiSimulation.java`

## 1) Start your API

The simulation expects the API at `http://localhost:4000` by default.

```bash
./mvnw spring-boot:run
```

## 2) Run the performance test

In another terminal:

```bash
./mvnw gatling:test -Dgatling.simulationClass=org.pm.smarty.performance.UserApiSimulation
```

## 3) Tune load from command line

```bash
./mvnw gatling:test \
  -Dgatling.simulationClass=org.pm.smarty.performance.UserApiSimulation \
  -DbaseUrl=http://localhost:4000 \
  -Dusers=200 \
  -DrampSeconds=45 \
  -DreadRps=40 \
  -DreadSeconds=120 \
  -Dp95Ms=1200 \
  -DsuccessRate=98.5
```

## Metrics and reports

After the run, Gatling generates an HTML report under:

- `target/gatling/<simulation-run-id>/index.html`
