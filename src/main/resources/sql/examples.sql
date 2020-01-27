-- delete from employee;
-- delete from company;

SELECT * FROM public.company;

ALTER TABLE public.company RENAME COLUMN formal_company_address TO company_address;

SELECT * FROM employee WHERE postal_address->>'postalCode' = '91052';