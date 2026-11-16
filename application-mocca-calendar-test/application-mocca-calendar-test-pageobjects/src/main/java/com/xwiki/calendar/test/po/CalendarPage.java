/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.xwiki.calendar.test.po;

import org.openqa.selenium.By;
import org.xwiki.model.reference.EntityReference;
import org.xwiki.test.ui.po.ViewPage;

/**
 * A page displaying a calendar, either a calendar page or a page using the calendar macro.
 *
 * @version $Id$
 * @since 2.22.1
 */
public class CalendarPage extends ViewPage
{
    private static final By ADD_CALENDAR_OBJECT = By.cssSelector(".add-calendar-object-container a");

    /**
     * Opens the given page.
     *
     * @param reference the reference of a page displaying a calendar
     * @return the page
     */
    public static CalendarPage gotoPage(EntityReference reference)
    {
        getUtil().gotoPage(reference);
        return new CalendarPage();
    }

    /**
     * Opens the given page and shows the current day (or the week, month containing it) in its calendar.
     *
     * @param reference the reference of a page displaying a calendar
     * @param view the calendar view to show
     * @return the calendar displayed in the page
     */
    public static CalendarElement gotoToday(EntityReference reference, CalendarElement.View view)
    {
        return gotoPage(reference).getCalendar().showToday(view);
    }

    /**
     * Creates (or replaces) a wiki page, e.g. to use the calendar macros with some parameters, without using the
     * editor, and opens it.
     *
     * @param reference the reference of the page
     * @param title the title of the page
     * @param content the content of the page, in XWiki 2.1 syntax
     * @return the saved page
     */
    public static CalendarPage create(EntityReference reference, String title, String content)
    {
        getUtil().createPage(reference, content, title);
        return gotoPage(reference);
    }

    /**
     * Creates a calendar page holding its own events, like the "Create new calendar" button does, but without using
     * the creation form, for the tests needing a calendar without testing its creation. Opens the new calendar.
     *
     * @param reference the reference of the calendar page, which must not exist
     * @param title the title of the calendar
     * @return the calendar page
     */
    public static CalendarPage createCalendar(EntityReference reference, String title)
    {
        // The content and the object of the calendar template, MoccaCalendar.MoccaCalendarTemplate.
        getUtil().createPage(reference, "{{moccacalendar filter=\"page\"/}}", title);
        getUtil().addObject(reference, "MoccaCalendar.MoccaCalendarClass");
        return gotoPage(reference);
    }

    /**
     * @return the calendar displayed in the page
     */
    public CalendarElement getCalendar()
    {
        return new CalendarElement();
    }

    /**
     * @param label the label of a calendar property, as displayed in the page (e.g. "Display the event author")
     * @return the value displayed for the given property
     */
    public String getPropertyValue(String label)
    {
        return EventPage.getFieldValue(getDriver().findElement(By.id("xwikicontent")), label);
    }

    /**
     * @return {@code true} if the page offers to turn the calendar macro into a dedicated calendar page
     */
    public boolean canAddCalendarObject()
    {
        return getDriver().hasElementWithoutWaiting(ADD_CALENDAR_OBJECT);
    }

    /**
     * Turns the page using the calendar macro into a dedicated calendar page.
     *
     * @return the reloaded page
     */
    public CalendarPage clickAddCalendarObject()
    {
        getDriver().addPageNotYetReloadedMarker();
        getDriver().findElement(ADD_CALENDAR_OBJECT).click();
        getDriver().waitUntilPageIsReloaded();
        return new CalendarPage();
    }
}
