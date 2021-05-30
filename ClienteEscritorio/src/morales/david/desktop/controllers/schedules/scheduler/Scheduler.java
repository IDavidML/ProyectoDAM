package morales.david.desktop.controllers.schedules.scheduler;

import morales.david.desktop.managers.DataManager;
import morales.david.desktop.managers.eventcallbacks.ScheduleConfirmationListener;
import morales.david.desktop.managers.eventcallbacks.ScheduleErrorListener;
import morales.david.desktop.managers.eventcallbacks.EventManager;
import morales.david.desktop.managers.SocketManager;
import morales.david.desktop.managers.eventcallbacks.ScheduleSwitchConfirmationListener;
import morales.david.desktop.models.*;
import morales.david.desktop.models.packets.Packet;
import morales.david.desktop.models.packets.PacketBuilder;
import morales.david.desktop.models.packets.PacketType;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    public static int MORNING_START_HOUR = 1;
    public static int MORNING_BREAK_HOUR = 13;
    public static int MORNING_END_HOUR = 6;

    public static int AFTERNOON_START_HOUR = 7;
    public static int AFTERNOON_BREAK_HOUR = 14;
    public static int AFTERNOON_END_HOUR = 12;

    private static final int DAY_LENGTH = 5;
    private static final int HOURS_LENGTH = 7;

    private boolean isMorning;

    private Hour[] hours;
    private Day[] days;
    private SchedulerItem[][] schedules;

    private SchedulerPair parentPair;

    public Scheduler(SchedulerPair parentPair, boolean isMorning) {
        this.parentPair = parentPair;
        this.isMorning = isMorning;
        init();
    }

    private void init() {

        {

            days = new Day[DAY_LENGTH];

            days = parentPair.getDayList().toArray(days);

        }

        {

            hours = new Hour[HOURS_LENGTH];

            int displacement = isMorning ? 0 : 6;

            hours[0] = parentPair.getHourList().get(0 + displacement);
            hours[1] = parentPair.getHourList().get(1 + displacement);
            hours[2] = parentPair.getHourList().get(2 + displacement);
            hours[3] = parentPair.getHourList().get(isMorning ? 12 : 13);
            hours[4] = parentPair.getHourList().get(3 + displacement);
            hours[5] = parentPair.getHourList().get(4 + displacement);
            hours[6] = parentPair.getHourList().get(5 + displacement);

        }

        {

            schedules = new SchedulerItem[DAY_LENGTH][HOURS_LENGTH];

            for(int day = 0; day < DAY_LENGTH; day++) {

                for(int hour = 0; hour < HOURS_LENGTH; hour++) {

                    SchedulerItem schedulerItem = findSchedule(day + 1, hour + 1);

                    if(schedulerItem != null) {

                        schedules[day][hour] = schedulerItem;

                    }

                }

            }

        }

    }

    public void switchSchedule(int i1, int j1, int i2, int j2) {

        SchedulerItem schedule1 = schedules[i1][j1];

        if(schedule1 == null)
            return;

        SchedulerItem schedule2 = schedules[i2][j2];

        if(schedule2 == null) {

            deleteSchedule(i1, j1);
            setScheduleFinal(schedule1, i2, j2);

        } else {

            if(schedule1 == schedule2)
                return;

            EventManager.getInstance().subscribe(schedule1.getUuid() + "|" + schedule2.getUuid(), (eventType, scheduleListenerType) -> {

                if(scheduleListenerType instanceof ScheduleSwitchConfirmationListener) {

                    ScheduleSwitchConfirmationListener switchConfirmationListener = (ScheduleSwitchConfirmationListener) scheduleListenerType;

                    schedules[i1][j1] = null;
                    schedules[i2][j2] = null;

                    schedules[i1][j1] = switchConfirmationListener.getSchedule2();
                    schedules[i2][j2] = switchConfirmationListener.getSchedule1();

                    parentPair.getTimetableManager().getGui().displayCurrentTimetable();

                } else if (scheduleListenerType instanceof ScheduleErrorListener) {

                    ScheduleErrorListener errorListener = (ScheduleErrorListener) scheduleListenerType;

                    System.out.println(errorListener.getMessage());

                }

            });

            Packet switchScheduleRequestPacket = new PacketBuilder()
                    .ofType(PacketType.SWITCHSCHEDULE.getRequest())
                    .addArgument("scheduleItem1", schedule1)
                    .addArgument("scheduleItem2", schedule2)
                    .build();

            SocketManager.getInstance().sendPacketIO(switchScheduleRequestPacket);

        }

    }

    private SchedulerItem findSchedule(int day, int hour) {
        int displacement = isMorning ? 0 : 6;
        for(SchedulerItem schedulerItem : parentPair.getScheduleList()) {
            for(Schedule schedule : schedulerItem.getScheduleList()) {
                if (schedule.getTimeZone().getDay().getId() == day && schedule.getTimeZone().getHour().getId() == (hour + displacement)) {
                    return schedulerItem;
                }
            }
        }
        return null;
    }

    public Day[] getDays() {
        return days;
    }

    public Hour[] getHours() {
        return hours;
    }

    public SchedulerItem[][] getSchedules() {
        return schedules;
    }

    public SchedulerItem getSchedule(int day, int hour) {
        return schedules[day][hour];
    }

    public String getScheduleText(int day, int hour) {

        SchedulerItem schedulerItem = schedules[day][hour];

        if(schedulerItem == null || schedulerItem.getScheduleList().size() == 0)
            return "";

        StringBuilder sb = new StringBuilder();

        for(Schedule schedule : schedulerItem.getScheduleList()) {

            sb.append(schedule.getTeacher().getName());
            sb.append("\n");
            sb.append(schedule.getSubject().getAbreviation() + "     " + schedule.getClassroom().toString());
            sb.append("\n");
            sb.append(schedule.getGroup().toString() + "     " + schedule.getTimeZone().getId());

        }

        return sb.toString();

    }

    public void setSchedule(SchedulerItem schedule, int day, int hour) {

        SchedulerItem oldSchedule = schedules[day][hour];

        if(oldSchedule == null || oldSchedule.getScheduleList().size() == 0) {

            setScheduleFinal(schedule, day, hour);

        } else {

            EventManager.getInstance().subscribe(oldSchedule.getUuid(), (eventType, scheduleListenerType) -> {

                if(scheduleListenerType instanceof ScheduleConfirmationListener) {

                    schedules[day][hour] = null;
                    setScheduleFinal(schedule, day, hour);

                } else if (scheduleListenerType instanceof ScheduleErrorListener) {

                    ScheduleErrorListener errorListener = (ScheduleErrorListener) scheduleListenerType;

                    System.out.println(errorListener.getMessage());

                }

            });

            Packet deleteScheduleRequestPacket = new PacketBuilder()
                    .ofType(PacketType.REMOVESCHEDULE.getRequest())
                    .addArgument("scheduleItem", oldSchedule)
                    .build();

            SocketManager.getInstance().sendPacketIO(deleteScheduleRequestPacket);

        }

    }
    private void setScheduleFinal(SchedulerItem schedulerItem, int day, int hour) {

        TimeZone newTimeZone = getTimeZoneBy(day + 1, hour + 1);
        TimeZone oldTimeZone = schedulerItem.getScheduleList().get(0).getTimeZone();

        for(Schedule schedule : schedulerItem.getScheduleList())
            schedule.setTimeZone(newTimeZone);

        if (schedulerItem.getScheduleList().size() > 0) {

            EventManager.getInstance().subscribe(schedulerItem.getUuid(), (eventType, scheduleListenerType) -> {

                if (scheduleListenerType instanceof ScheduleConfirmationListener) {

                    schedules[day][hour] = schedulerItem;
                    parentPair.getTimetableManager().getGui().displayCurrentTimetable();

                } else if (scheduleListenerType instanceof ScheduleErrorListener) {

                    ScheduleErrorListener errorListener = (ScheduleErrorListener) scheduleListenerType;

                    System.out.println(errorListener.getMessage());

                    for(Schedule schedule : schedulerItem.getScheduleList())
                        schedule.setTimeZone(oldTimeZone);

                }

            });

            Packet insertScheduleRequestPacket = new PacketBuilder()
                    .ofType(PacketType.INSERTSCHEDULE.getRequest())
                    .addArgument("scheduleItem", schedulerItem)
                    .build();

            SocketManager.getInstance().sendPacketIO(insertScheduleRequestPacket);

        }

    }

    public void deleteSchedule(int indexDay, int indexHour) {

        SchedulerItem schedulerItem = schedules[indexDay][indexHour];

        if(schedulerItem != null && schedulerItem.getScheduleList().size() > 0) {

            EventManager.getInstance().subscribe(schedulerItem.getUuid(), (eventType, scheduleListenerType) -> {

                if(scheduleListenerType instanceof ScheduleConfirmationListener) {

                    schedules[indexDay][indexHour] = null;
                    parentPair.getTimetableManager().getGui().displayCurrentTimetable();

                } else if (scheduleListenerType instanceof ScheduleErrorListener) {

                    ScheduleErrorListener errorListener = (ScheduleErrorListener) scheduleListenerType;

                    System.out.println(errorListener.getMessage());

                }

            });

            Packet deleteScheduleRequestPacket = new PacketBuilder()
                    .ofType(PacketType.REMOVESCHEDULE.getRequest())
                    .addArgument("scheduleItem", schedulerItem)
                    .build();

            SocketManager.getInstance().sendPacketIO(deleteScheduleRequestPacket);

        }

    }

    private TimeZone getTimeZoneBy(int day, int hour) {
        for(TimeZone timeZone : new ArrayList<>(DataManager.getInstance().getTimeZones())) {
            if(timeZone.getDay().getId() == day && timeZone.getHour().getId() == hour) {
                return timeZone;
            }
        }
        return null;
    }

}
