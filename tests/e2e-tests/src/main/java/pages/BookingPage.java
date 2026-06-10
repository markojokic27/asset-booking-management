package pages;

import commonmethods.CommonMethods;
import org.openqa.selenium.By;

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
    public By fromHourSelect = By.cssSelector("select[aria-label*='From']");
    public By toHourSelect = By.cssSelector("select[aria-label*='To']");
    public By checkBoxDays = By.cssSelector("[data-testid='checkbox-days-label']");

    // Book button on asset page
    public By bookAssetButton = By.cssSelector("[data-testid='book-asset-button']");

    // Calendar
    public By calendar = By.cssSelector(".fc-dayGridMonth-view");
    public By calendarNext = By.cssSelector(".fc-next-button");
    public By calendarPrev = By.cssSelector(".fc-prev-button");
    public By calendarTitle = By.cssSelector(".fc-toolbar-title");

    public By meetingRoomCategoryCard = By.cssSelector("[data-testid='category-card-meeting room']");

    // Parking map
    public By parkingMapButton = By.cssSelector("[data-testid='parking-map-button']");
    public By parkingMapModal = By.cssSelector("[aria-label='Parking map']");
    public By parkingMapCloseButton = By.cssSelector("[data-testid='parking-close-button']");
    public By floorLevelMinus1Active = By.cssSelector("[data-testid='level-button--1'].bg-white");
    public By floorLevelMinus2Active = By.cssSelector("[data-testid='level-button--2'].bg-white");
    public By categoryParkingCard = By.cssSelector("[data-testid='category-card-parking']");

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

    public void selectFromHour(String hour) {
        clickOnElement(fromHourSelect);
        selectByVisibleText(fromHourSelect, hour);
        clickOnElement(fromHourSelect);
    }

    public void selectToHour(String hour) {
        clickOnElement(toHourSelect);
        selectByVisibleText(toHourSelect, hour);
        clickOnElement(toHourSelect);
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

    public boolean isCalendarCellPast(String dateStr) {
        return elementHasClass(calendarCellLocator(dateStr), "opacity-60");
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
    public void clickMeetingRoomCategory() {
        clickOnElement(meetingRoomCategoryCard);
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