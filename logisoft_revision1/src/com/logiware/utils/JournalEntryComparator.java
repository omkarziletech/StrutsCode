package com.logiware.utils;

import com.gp.cvst.logisoft.domain.JournalEntry;
import java.util.Comparator;

public class JournalEntryComparator
        implements Comparator<JournalEntry> {

    public int compare(JournalEntry o1, JournalEntry o2) {
        String s1 = o1.getJournalEntryId();
        String s2 = o2.getJournalEntryId();
        return s1.compareTo(s2);
    }
}