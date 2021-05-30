package com.example.quakereport;

public class Event {
    public final String title;
    public final String time;
    public final double tsunamiAlert;

    /**
     * Constructs a new {@link Event}.
     *
     * @param eventTitle is the title of the earthquake event
     * @param eventTime is the time the earhtquake happened
     * @param eventTsunamiAlert is whether or not a tsunami alert was issued
     */
    public Event(String eventTitle, String eventTime, double eventTsunamiAlert) {
        title = eventTitle;
        time = eventTime;
        tsunamiAlert = eventTsunamiAlert;
    }

    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", tsunamiAlert=" + tsunamiAlert +
                '}';
    }
}
