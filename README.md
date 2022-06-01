# pact-demo

## Run Pact Tests

```
# Generate PACTs
cd consumer-kafka && gradle test
cd consumer-rest && gradle test
ls pacts

# Run Provider against PACTs
cd provider && gradle test
```
