package ru.geekbrains;

public class SampleTest {

    @BeforeSuite
    public void sampleBeforeSuite() {
        System.out.println("This is BeforeSuit");
    }

    @Test(priority = 1)
    public void sampleTest1() {
        System.out.println("This is sample test priority 1");
    }
    @Test(priority = 3)
    public void sampleTest3() {
        System.out.println("This is sample test priority 3");
    }
    @Test(priority = 2)
    public void sampleTest() {
        System.out.println("This is sample test priority 2");
    }

    @AfterSuite
    public void sampleAfterSuite() {
        System.out.println("This is AfterSuit");
    }
}
