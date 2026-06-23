package pages;

import commonmethods.CommonMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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

    // Calendar
    public By calendar = By.cssSelector(".fc-dayGridMonth-view");
    public By calendarNext = By.cssSelector(".fc-next-button");
    public By calendarPrev = By.cssSelector(".fc-prev-button");
    public By calendarTitle = By.cssSelector(".fc-toolbar-title");

    public By itEquipmentCategoryCard = By.cssSelector("[data-testid='category-card-it equipment']");

    // Parking map
    public By parkingMapButton = By.cssSelector("[data-testid='parking-map-button']");
    public By parkingMapCloseButton = By.cssSelector("[data-testid='parking-close-button']");
    public By floorLevelMinus1Active = By.cssSelector("[data-testid='level-button--1'].bg-white");
    public By floorLevelMinus2Active = By.cssSelector("[data-testid='level-button--2'].bg-white");
    public By categoryParkingCard = By.cssSelector("[data-testid='category-card-parking']");

    // Parking map - spot popover
    public By spotPopover = By.cssSelector("[data-testid='spot-popover']");
    public By parkingSpotStatus = By.cssSelector("[data-testid='parking-spot-status']");
    public By parkingMapDateInput = By.cssSelector("input[type='date']");
    public By spotPopoverBookButton = By.cssSelector("[data-testid='spot-book-button']");
    public By spotPopoverCloseButton = By.cssSelector("[data-testid='spot-popover-close-button']");
    public By spotPopoverBackdrop = By.cssSelector("[data-testid='spot-popover-backdrop']");

    public void closeSpotPopover() {
        if (isElementVisible(spotPopoverBackdrop)) {
            clickOnElement(spotPopoverBackdrop);
        } else if (isElementVisible(spotPopoverCloseButton)) {
            clickOnElement(spotPopoverCloseButton);
        }
    }

    public void clickSpotBookButton() {
        clickOnElement(spotPopoverBookButton);
    }

    public void selectParkingMapDate(String date) {
        inputDate(parkingMapDateInput, date);
    }

    public void clickParkingSpot(int spotNumber) {
        clickOnElement(By.cssSelector("[data-testid='parking-spot-" + spotNumber + "']"));
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
        throw new RuntimeException("Nema slobodnih parking spotova za odabrani datum!");
    }

    // Parking filter
    private By calendarCellLocator(String dateStr) {
        return By.cssSelector("[data-date='" + dateStr + "']");
    }

    public void clickBookButton() {
        clickOnElement(bookButton);
    }

    public void clickBookAssetButton() {
        clickOnElement(bookAssetButton);
    }

    public void searchAssets(String keyword) {
        typeInElement(searchField, keyword);
    }

    public void clickResetFilters() {
        clickOnElement(resetFiltersButton);
    }

    // Filter
    public void enterFromDate(String date) {
        inputDate(fromDateInput, date);
    }

    public void enterToDate(String date) {
        inputDate(toDateInput, date);
    }

    public String getFromDateValue() {
        return getDriver().findElement(fromDateInput).getAttribute("value");
    }

    // Calendar
    public boolean isCalendarVisible() {
        return isElementVisible(calendar);
    }

    public void clickCalendarDate(String dateStr) {
        clickOnElement(calendarCellLocator(dateStr));
    }

    public boolean isCalendarCellSelected(String dateStr) {
        return elementHasClass(calendarCellLocator(dateStr), "ring-2");
    }

    public void clickNextMonth() {
        clickOnElement(calendarNext);
    }

    public void clickPrevMonth() {
        clickOnElement(calendarPrev);
    }

    // Book asset button
    public boolean isBookAssetButtonEnabled() {
        return isElementEnabled(bookAssetButton);
    }

    // Meeting room
    public void clickitEquipmentCategory() {
        clickOnElement(itEquipmentCategoryCard);
    }

    // Parking
    public void clickParkingCategory() {
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

    public void clickCheckBoxDays() {
        clickOnElement(checkBoxDays);
    }
}