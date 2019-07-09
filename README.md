# Fio Bank API client
Modern implementation of [FIO](https://www.fio.cz) [Bank API](http://www.fio.cz/bank-services/internetbanking-api).

Implemented in Kotlin with pure Java API. Any of HTTPS client library can be used, as like the JSON parser.

## Usage

1. Add a library to your project.
2. Implement requested interfaces `FioWebConnector` and `FioJsonConverter`
3. Create instance of `FioClient` with your token

### Creating instance
```java
FioClient fio = FioClient(urlConnector, gson, "mytoken");
```

### Get account statement

Get account statement with **the year and statement number**:
```java
FioAccountStatement statement = fio.getStatement(2019, 1);
```

### Get list of transactions

Get list of transactions for **the given period**:
```java
FioAccountStatement statement = fio.getTransactions(new LocalDate(2019, 1, 1), new LocalDate(2019, 1, 31));
```

Get list of transactions from the **last download**:
```java
FioAccountStatement statement = fio.newTransactions();
```

### Set last downloaded transaction

Set last downloaded transaction by **by date**:
```java
fio.setTransactionPointerByDate(new LocalDate(2019, 1, 1));
```

Set last downloaded transaction **by transaction id**:
```java
fio.setTransactionPointerById("123456789");
```

## Documentation
[API documentation from FIO](http://www.fio.cz/docs/cz/API_Bankovnictvi.pdf)

## Credits
- Jan Pěček

## Licence
Licensed by [GNU GPL v3](LICENSE)
