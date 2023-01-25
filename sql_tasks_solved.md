# Must use:

- MySQL database
- db_schema.sql for DB creation

## 1.Task

- Get numbers of vehicles sold (loans disbursed) in Jan and Feb 2020 per each vehicle make.
- a) Display only those makes where total sales are more than 1000 units
- b) Display full sales data including all makes (including those where sales are not made)

## 2. Task

- Get current weekly payment amount for each loan. Table m_loan_repayment_schedule contains weekly payment records.
  Weekly payment record should be selected for the first week where obligations are not met (value for field
  completed_derived=0).
  Use principal_amount plus interest_amount to acquire weekly payment amount.

## 3. Task

- Calculate current balance (scheduled amount - payed amount) for each loan. Use tables m_loan_repayment_schedule for
  payment schedule data.
  Total scheduled payment amount on current date must be calculated by sum of all amount field values.
  Some values can be null. Payment data are stored in table m_loan_transaction.

# Solutions

## 1.

### A)

SELECT sales, name FROM (  
SELECT COUNT(a.m_loan_id) as sales, vm2.name as name FROM \`asset-schema\`.asset a   
JOIN \`loan-schema\`.m_loan ml on a.m_loan_id = ml.id   
JOIN \`asset-schema\`.vehicle_model vm on a.model_id = vm.id  
JOIN \`asset-schema\`.vehicle_make vm2 on vm.vehicle_make_id = vm2.id   
WHERE ml.disbursedon_date >= '2020-1-1' AND ml.disbursedon_date < '2020-3-1'  
GROUP BY vm2.name ) x  
WHERE sales > 1000 ;

### B)

SELECT COUNT(a.m_loan_id) as sales, vm2.name as name FROM \`asset-schema\`.asset a   
JOIN \`loan-schema\`.m_loan ml on a.m_loan_id = ml.id   
JOIN \`asset-schema\`.vehicle_model vm on a.model_id = vm.id   
RIGHT JOIN \`asset-schema\`.vehicle_make vm2 on vm.vehicle_make_id = vm2.id   
WHERE ml.disbursedon_date >= '2020-1-1' AND ml.disbursedon_date < '2020-3-1'  
GROUP BY vm2.name ;

## 2.

SELECT (mlrs.principal_amount + mlrs.interest_amount) AS weekly_payment, mlrs.loan_id, mlrs.duedate    
FROM \`loan-schema\`.m_loan_repayment_schedule mlrs   
WHERE mlrs.duedate = (SELECT MIN(mlrs2.duedate)   
FROM \`loan-schema\`.m_loan_repayment_schedule mlrs2   
WHERE mlrs2.completed_derived = 0 AND mlrs2.loan_id = mlrs.loan_id   
GROUP BY mlrs.loan_id);

## 3.

SELECT mlrs.loan_id, SUM(mlrs.principal_amount + mlrs.interest_amount + mlrs.fee_charges_amount +
mlrs.penalty_charges_amount) - SUM(mlt.amount) AS current_balance  
FROM \`loan-schema\`.m_loan_repayment_schedule mlrs  
JOIN \`loan-schema\`.m_loan_transaction mlt on mlrs.loan_id = mlt.loan_id   
GROUP BY mlrs.loan_id ;  


