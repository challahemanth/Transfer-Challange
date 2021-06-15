1. Exposed an rest endpoint "http://localhost:18080/v1/accounts/transferAmount" method post. Which needs the transfer domain object which cosists of fromAccount, toAccount and amount to be transfer

2. This endpoint validates the zero amount, negative amount and Account not present errors and balance insufficient erros.

3. Implemented re-entrant lock to make it thread safe

4. Implemented custom exceptions BalanceFailed and BalanceSucess exceptions

5. Re-used notificationServide to notify both account holders on sucesful transfer

6. Implemented test cases to demonstrate some of the test scenarios 