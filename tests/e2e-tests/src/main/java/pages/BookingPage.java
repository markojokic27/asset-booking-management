package pages;

import commonmethods.CommonMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class BookingPage extends CommonMethods {

    public BookingPage() {
        super();
    }

    // Locators
    public By bookButton = By.cssSelector("[data-testid='book-button']");
    public By searchField = By.cssSelector("[data-testid='search-input']");
    public By resetFiltersButton = By.cssSelector("[data-testid='reset-filters-button']");

    // Filter
    public By fromDateInput = By.cssSelector("[data-testid='from-date-input']");
    public By toDateInput = By.cssSelector("[data-testid='to-date-input']");
    public By checkBoxDays = By.cssSelector("[data-testid='checkbox-days-label']");

    // Book button on asset page
    public By bookAssetButton = By.cssSelector("[data-testid='book-asset-button']");
    public By bookNowButton = By.cssSelector("[data-testid='book-now-button']");
    public By cancelBookButton = By.cssSelector("[role='dialog'] [data-testid='cancel-button']");

    // Calendar
    public By calendar = By.cssSelector(".fc-dayGridMonth-view");
    public By calendarNext = By.cssSelector(".fc-next-button");
    public By calendarPrev = By.cssSelector(".fc-prev-button");
    public By calendarTitle = By.cssSelector(".fc-toolbar-title");

    public By itEquipmentCategoryCard = By.cssSelector("[data-testid='category-card-it equipment']");
    public By laptopCategoryCard = By.cssSelector("[data-testid='category-card-laptop']");


    // Parking map
    public By parkingMapButton = By.cssSelector("[data-testid='parking-map-button']");
    public By parkingMapCloseButton = By.cssSelector("[data-testid='parking-close-button']");
    public By floorLevelMinus1Active = By.cssSelector("[data-testid='level-button--1'].bg-white");
    public By floorLevelMinus2Active = By.cssSelector("[data-testid='level-button--2'].bg-white");
    public By categoryParkingCard = By.cssSelector("[data-testid='category-card-parking']");

    public By spotPopover = By.cssSelector("[data-testid='parking-spot-status']");
    public By parkingSpotStatus = By.cssSelector("[data-testid='parking-spot-status']");
    public By parkingMapDateInput = By.cssSelector("input[type='date']");
    public By spotPopoverBookButton = By.cssSelector("[data-testid='spot-book-button']");


    public void clickSpotBookButton() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(spotPopoverBookButton));
        jsClick(element);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(parkingSpotStatus));
    }

    public void selectParkingMapDate(String date) {
        inputDate(parkingMapDateInput, date);
    }

    // Parking filter
    private By calendarCellLocator(String dateStr) {
        return By.cssSelector("[data-date='" + dateStr + "']");
    }

    public void clickBookButton() {
        clickOnElement(bookButton);
        isElementVisible(bookAssetButton);
    }

    public void searchAssets(String keyword) {
        typeInElement(searchField, keyword);
    }

    public void clickResetFilters() {
        WebElement element = driver.findElement(resetFiltersButton);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element);
        jsClick(element);
    }

    // Filter
    public void enterFromDate(String date) {
        inputDate(fromDateInput, date);
    }

    public void enterToDate(String date) {
        inputDate(toDateInput, date);
    }

    // Calendar
    public boolean isCalendarVisible() {
        return isElementVisible(calendar);
    }

    public void clickCalendarDate(String dateStr) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(calendarCellLocator(dateStr)));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element);
        jsClick(element);
    }

    public void clickNextMonth() {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(calendarNext));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element);
        jsClick(element);
    }

    public boolean isCalendarCellSelected(String dateStr) {
        return elementHasClass(calendarCellLocator(dateStr), "ring-2");
    }

    public void clickPrevMonth() {
        clickOnElement(calendarPrev);
    }

    // Book asset button
    public boolean isBookAssetButtonEnabled() {
        return isElementEnabled(bookAssetButton);
    }

    // Meeting room
    public void clickItEquipmentCategory() {
        clickOnElement(itEquipmentCategoryCard);
    }

    public void clickLaptopCategory (){clickOnElement(laptopCategoryCard);}

    // Parking
    public void clickParkingCategory() {
        isElementVisible(categoryParkingCard);
        clickOnElement(categoryParkingCard);
    }

    public void clickParkingMapButton() {
        clickOnElement(parkingMapButton);
    }

    public void closeParkingMapModal() {
        clickOnElement(parkingMapCloseButton);
    }

    public void clickFloorLevel(String level) {
        clickOnElement(By.cssSelector("[data-testid='level-button-" + level + "']"));
    }

    public void clickBookButtonForInactiveAsset() {
        clickOnElement(By.xpath("//td[normalize-space()='Inactive']/following-sibling::td//button"));
    }

    public void selectAllRecurringDays() {
        getDriver().findElements(checkBoxDays).forEach(BookingPage::jsClick);
    }

    public void clickParkingSpot(int spotNumber) {
        By backdrop = By.cssSelector("[data-testid='spot-popover-backdrop']");
        List<WebElement> backdrops = driver.findElements(backdrop);
        if (!backdrops.isEmpty() && backdrops.getFirst().isDisplayed()) {
            jsClick(backdrops.getFirst());
            wait.until(ExpectedConditions.invisibilityOfElementLocated(backdrop));
        }
        clickOnElement(By.cssSelector("[data-testid='parking-spot-" + spotNumber + "']"));
    }

    public void clickCheckBoxDays() {
        WebElement element = driver.findElement(checkBoxDays);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element);
        jsClick(element);
    }

    public void clickCancelBookButton() {
        List<WebElement> buttons = driver.findElements(cancelBookButton);
        for (WebElement btn : buttons) {
            if (btn.isDisplayed()) {
                jsClick(btn);
                return;
            }
        }
    }

    public int getFirstAvailableParkingSpot() {
        List<WebElement> allSpots = getDriver().findElements(
                By.cssSelector("[data-testid^='parking-spot-']"));

        for (WebElement spot : allSpots) {
            String testId = spot.getAttribute("data-testid");
            if (testId == null || !testId.matches("parking-spot-\\d+")) continue;

            List<WebElement> rects = spot.findElements(By.tagName("rect"));
            if (rects.isEmpty()) continue;

            String fill = rects.getFirst().getAttribute("fill");
            if (fill != null && fill.equalsIgnoreCase("#F97316")) continue;

            String numberStr = testId.replace("parking-spot-", "");
            return Integer.parseInt(numberStr);
        }
        throw new RuntimeException("No free parking spots for the selected day!");
    }

    public void clickBookAssetButton() {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(bookAssetButton));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element);
        jsClick(element);
    }

    public void clickBookNowButton() {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(bookNowButton));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element);
        jsClick(element);
    }

}