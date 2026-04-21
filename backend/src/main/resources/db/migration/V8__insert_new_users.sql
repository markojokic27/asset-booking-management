-- Insert users (bcrypt hashes required)
INSERT INTO asset_user (
    username,
    password,
    name,
    surname,
    email,
    status,
    role,
    notes,
    benefit,
    manager_email,
    department_id
)
SELECT
    v.username,
    v.password,
    v.name,
    v.surname,
    v.email,
    v.status,
    v.role,
    v.notes,
    v.benefit,
    v.manager_email,
    d.id
FROM (
         VALUES
             (
                'mBanovic',
                '$2a$12$pMy0lnfG7RnzwDT3Nhf8/Oi2/BKoQadL6m0CJTOBCUaMekOuQ7KGW', -- bcrypt of mladen123
                'Mladen',
                'Banovic',
                'mladen.banovic@example.com',
                'ACTIVE',
                'MANAGER',
                'This is a dummy Mladen account',
                'ALL',
                'manager.doe@example.com',
                'ADVANCE_TECHNOLOGY'
             ),
             (
                'aMustapic',
                '$2a$12$l15.tTGmBmoHkc.vxmLTqObXY4zchgFL/ZFXdf457VuL8.csEwCx2', -- bcrypt of andela123
                'Andela',
                'Mustapic',
                'andela.mustapic@example.com',
                'STUDENT',
                'EMPLOYEE',
                'This is a dummy Andela account',
                'ALL',
                'mladen.banovic@example.com',
                'SYSTEM_TEST'
             ),
             (
                'dJezidzic',
                '$2a$12$ExyBg6kzEU1Q93liOwG4qO.sWrxGEMG6Gxajsjf273ERAkbYM3xZO', -- bcrypt of david123
                'David',
                'Jezidzic',
                'david.jezidzic@example.com',
                'STUDENT',
                'ADMIN',
                'This is a dummy David account',
                'ALL',
                'mladen.banovic@example.com',
                'DEVOPS'
             ),
             (
                'jPetric',
                '$2a$12$hDfCN4E.M8QicBIo6p599uh1Ntxqx9mJdpNDDSqP6reoj4eQEacku', -- bcrypt of jakov123
                'Jakov',
                'Petric',
                'jakov.petric@example.com',
                'STUDENT',
                'EMPLOYEE',
                'This is a dummy Jakov account',
                'ALL',
                'mladen.banovic@example.com',
                'CLOUD_AND_DATA_MANAGEMENT'
             ),
             (
                'jDiko',
                '$2a$12$Y4Ok9FyLScMv1yJ9Soz/dexsAyBuwUAFVyRCApNCjB1WwP2K33vuS', -- bcrypt of josko123
                'Josko',
                'Diko',
                'josko.diko@example.com',
                'STUDENT',
                'EMPLOYEE',
                'This is a dummy Josko account',
                'ALL',
                'mladen.banovic@example.com',
                'HUMAN_RESOURCES'
             ),
             (
                'kNovakovic',
                '$2a$12$jVlkOO3nofN57OzKsYwaLeLMGwLTA/YqFTSnGvI/j6B5SNq7wNIK.', -- bcrypt of katarina123
                'Katarina',
                'Novakovic',
                'katarina.novakovic@example.com',
                'STUDENT',
                'EMPLOYEE',
                'This is a dummy Katarina account',
                'ALL',
                'mladen.banovic@example.com',
                'FINANCE_AND_BUSINESS_ADMINISTRATION'
             ),
             (
                'mJokic',
                '$2a$12$M3KtX92YKiGlTqLHWJY/DeblyrFjRAlAlcX/okYtJ7MYS4kh3Sf9q', -- bcrypt of marko123
                'Marko',
                'Jokic',
                'marko.jokic@example.com',
                'STUDENT',
                'EMPLOYEE',
                'This is a dummy Marko account',
                'ALL',
                'mladen.banovic@example.com',
                'ARCHITECTURE'
             ),
             (
                'mPlavcic',
                '$2a$12$4PbDsj5Xn6oC7Iot.xjy6.lSxa/K/cSUsOdvX1jj.noWH825LuOje', -- bcrypt of mateo123
                'Mateo',
                'Plavcic',
                'mateo.plavcic@example.com',
                'STUDENT',
                'EMPLOYEE',
                'This is a dummy Mateo account',
                'ALL',
                'mladen.banovic@example.com',
                'SECURE_SERVICES'
             ),
             (
                'mMajic',
                '$2a$12$7Ibo2k6msrAwFXTgxEmGjeQvZBtJvzPndx3ckvYuEq.MEesp2ctJ6', -- bcrypt of monika123
                'Monika',
                'Majic',
                'monika.majic@example.com',
                'STUDENT',
                'EMPLOYEE',
                'This is a dummy Monika account',
                'ALL',
                'mladen.banovic@example.com',
                'MOBILE_AND_SECURITY'
             ),
             (
                'nCarevic',
                '$2a$12$0fGQHVWkfbDPAejm.VjBNOJuXG56ViZdtNKtP8rjOdF2MEDcYzSqa', -- bcrypt of nikolina123
                'Nikolina',
                'Carevic',
                'nikolina.carevic@example.com',
                'STUDENT',
                'EMPLOYEE',
                'This is a dummy Nikolina account',
                'ALL',
                'mladen.banovic@example.com',
                'FINANCE_AND_BUSINESS_ADMINISTRATION'
             ),
             (
                'zKujundzic',
                '$2a$12$J.3tuMZ76JsvQ4bmpkHpF.KXNJi1V6HAh077mGv8gShpVj1zcjYkq', -- bcrypt of zvone123
                'Zvonimir',
                'Kujundzic',
                'zvone.kujundzic@example.com',
                'STUDENT',
                'ADMIN',
                'This is a dummy Zvone account',
                'ALL',
                'mladen.banovic@example.com',
                'DEVOPS'
             )
     ) AS v(
         username,
         password,
         name,
         surname,
         email,
         status,
         role,
         notes,
         benefit,
         manager_email,
         department_name
    )
        JOIN department d ON d.name = v.department_name
    ON CONFLICT (username) DO NOTHING;
