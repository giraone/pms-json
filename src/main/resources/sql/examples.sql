-- delete from employee;
-- delete from company;

SELECT COUNT(*) FROM Company;
SELECT COUNT(*) FROM Employee;

SELECT * FROM Company LIMIT 10;
SELECT * FROM Employee LIMIT 10;

DELETE FROM Employee;
DELETE FROM Company;


SELECT * FROM employee WHERE postal_address->>'postalCode' = '91052';