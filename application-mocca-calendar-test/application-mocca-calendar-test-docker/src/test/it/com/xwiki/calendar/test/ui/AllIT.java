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
package com.xwiki.calendar.test.ui;

import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestClassOrder;
import org.xwiki.test.docker.junit5.UITest;

/**
 * All UI tests for the Calendar application.
 *
 * @version $Id$
 * @since 2.11
 */
@UITest(extraJARs = {
    // The version of the extra JARs must be given, otherwise the version of this project is used: keep it in sync with
    // the XWiki version of the parent POM.
    // XWiki needs the plugin JARs (mailsender, jodatime) to be present before it starts since they're not extensions
    // and they cannot be provisioned after XWiki is started!
    "org.xwiki.platform:xwiki-platform-mailsender:15.10",
    // The calendar formats the event dates with the jodatime plugin.
    "org.xwiki.platform:xwiki-platform-jodatime:15.10",
    // MailSender plugin uses MailSenderConfiguration from xwiki-platform-mail-api so we need to provide an
    // implementation for it.
    "org.xwiki.platform:xwiki-platform-mail-send-default:15.10",
    // MoccaCalendar.Code.MoccaCalendarLiveTableResults uses the "document" parameter of the include macro, which only
    // the legacy module provides. A standard XWiki distribution bundles it in the WAR, but the test WAR doesn't, and it
    // cannot be installed as an extension because it replaces a core extension.
    "org.xwiki.platform:xwiki-platform-legacy-rendering-macro-include:15.10" }, resolveExtraJARs = true, properties = {
    // Plugins are not registered just by being on the classpath: they must also be listed in xwiki.cfg.
    "xwikiCfgPlugins=com.xpn.xwiki.plugin.jodatime.JodaTimePlugin,"
        + "com.xpn.xwiki.plugin.mailsender.MailSenderPlugin",
    // The test framework denies programming rights to every page, to catch the code that silently needs them. The
    // event sheet does: it reads the event objects through Document#getDocument(), which requires programming rights.
    "xwikiPropertiesAdditionalProperties=test.prchecker.excludePattern=.*:MoccaCalendar\\.MoccaCalendarEventSheet" })
// The test classes share the XWiki instance (and the browser), so run them always in the same order.
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class AllIT
{
    @Nested
    @Order(1)
    @DisplayName("Overall Calendar UI")
    class NestedCalendarIT extends CalendarIT
    {
    }

    @Nested
    @Order(2)
    @DisplayName("Calendar events: editing, colors, hours, drag & drop")
    class NestedCalendarEventIT extends CalendarEventIT
    {
    }

    @Nested
    @Order(3)
    @DisplayName("Recurrent events")
    class NestedRecurrentEventIT extends RecurrentEventIT
    {
    }

    @Nested
    @Order(4)
    @DisplayName("Calendar macros")
    class NestedCalendarMacroIT extends CalendarMacroIT
    {
    }

    @Nested
    @Order(5)
    @DisplayName("iCal export, subscription and import")
    class NestedICalIT extends ICalIT
    {
    }
}
