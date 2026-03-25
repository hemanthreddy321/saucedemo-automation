Excel test data files go in this directory.

Expected files:
  - LoginData.xlsx
      Sheet "Login":
        Columns: username | password | expectedResult
        Rows:    standard_user | secret_sauce | success
                 locked_out_user | secret_sauce | locked
                 standard_user | wrong | error

  - CheckoutData.xlsx
      Sheet "Checkout":
        Columns: firstName | lastName | postalCode
        Rows:    John | Doe | 560001
                 Jane | Smith | 400001
                 Hemanth | Kumar | 500001

Usage in step defs:
  List<Map<String,String>> rows = ExcelDataReader.readSheet(
      "src/test/resources/testdata/LoginData.xlsx", "Login");
