import { userValidationSchema } from '../../features/user/validation'

    describe("User schema validation", () => {
        test("should pass with valid data", () =>{
            const result = userValidationSchema.safeParse({
                username: "ivanivic",
                surname: "ivic",
                name: "ivan",
                email: "ivanivic@maurer-electonics.hr",
                password: "password.123",
                role: "EMPLOYEE",
                status: "ACTIVE",
                notes: "Some optional notes",
                departmentId: 5,
                managerEmail: "antem@maurer-electonics.hr",
                benefit: "ALL"
                });

            expect(result.success).toBe(true);

            });

    describe("Username validation", () => {

         test("should fail when username is empty", () =>{
        const result = userValidationSchema.safeParse({
            username: "",
            surname: "ivic",
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

         test("should fail when username is null", () =>{
        const result = userValidationSchema.safeParse({
            username: null,
            surname: "ivic",
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

        test("should fail when username is too long", () =>{
        const result = userValidationSchema.safeParse({
            username: "i.repeat(51)",
            surname: "ivic",
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

        test("should fail when username is too short", () =>{
        const result = userValidationSchema.safeParse({
            username: "i",
            surname: "ivic",
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

        test("should fail with invalid characters", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic!",
            surname: "ivic",
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

        test("should pass with allowed characters", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic40",
            surname: "ivic",
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(true);

        });

    })

    describe("Surname validation", () => {

        test("should fail when surname is empty ", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: "",
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });


          test("should fail when surname is null ", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: null,
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

        test("should fail when surname is null ", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: null,
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });


        test("should fail when surname is too long ", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: "a".repeat(101),
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

    })

    describe("Name validation", () => {

        test("should fail when name is empty ", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: "ivic",
            name: "",
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

        test("should fail when name is null ", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: "ivic",
            name: null,
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });


        test("should fail when name is too long ", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: "ivic",
            name: "a".repeat(101),
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

    })


    describe("Email validation", () => {

        test("should fail when email is empty ", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: "ivic",
            name: "ivan",
            email:"",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

         test("should fail when email is null ", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: "ivic",
            name: "ivan",
            email:null,
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });


        test("should fail when email is too long ", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: "ivic",
            name: "ivan",
            email: "a".repeat(255) + "@maurer-electronics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

        test("should fail with invalid email format ", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: "ivic",
            name: "ivan",
            email: "ivanivic",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

    })

    describe("Password validation", () => {

         test("should fail when password is empty", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: "ivic",
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

         test("should fail when password is null", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: "ivic",
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: null,
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

        test("should fail when password is too long", () =>{
        const result = userValidationSchema.safeParse({
            username: "a".repeat(51),
            surname: "ivic",
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "p".repeat(51),
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

        test("should fail when password is too short", () =>{
        const result = userValidationSchema.safeParse({
            username: "a",
            surname: "ivic",
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "p",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

        test("should pass with min length", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: "ivic",
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "pass.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(true);

        });
    })
    

    describe("Role validation", () => {

        test("should fail when role is null ", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: "ivic",
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: null,
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

    })

    describe("Status validation", () => {

        test("should fail when status is null ", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: "ivic",
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: null,
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

    })

    describe("DepartmentId validation", () => {

        test("should fail when departmentId is null ", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: "ivic",
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: null,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

    })

    
     describe("ManagerEmail validation", () => {

        test("should fail when managerEmail is empty ", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: "ivic",
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

        test("should fail when managerEmail is null ", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: "ivic",
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: null,
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });


        test("should fail when managerEmail is too long ", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: "ivic",
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "a".repeat(255)+"@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

        test("should fail with invalid managerEmail format ", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: "ivic",
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "Some optional notes",
            departmentId: 5,
            managerEmail: "antem",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

    })


    describe("Note validation", () => {

        test("should pass when notes is empty ", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: "ivic",
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "",
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(true);

        });

        test("should fail when notes is too long ", () =>{
        const result = userValidationSchema.safeParse({
            username: "ivanivic",
            surname: "ivic",
            name: "ivan",
            email: "ivanivic@maurer-electonics.hr",
            password: "password.123",
            role: "EMPLOYEE",
            status: "ACTIVE",
            notes: "a".repeat(1001),
            departmentId: 5,
            managerEmail: "antem@maurer-electonics.hr",
            benefit: "ALL"
            });

        expect(result.success).toBe(false);

        });

    })


});