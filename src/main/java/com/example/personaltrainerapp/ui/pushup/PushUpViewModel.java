package com.example.personaltrainerapp.ui.pushup;

import com.example.personaltrainerapp.database.DatabaseManager;
import com.example.personaltrainerapp.model.pushupbuddies.Friend;
import com.example.personaltrainerapp.model.entries.PushUpEntry;
import com.example.personaltrainerapp.model.User;
import com.example.personaltrainerapp.repository.FriendRepository;
import com.example.personaltrainerapp.repository.PushUpRepository;
import com.example.personaltrainerapp.repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PushUpViewModel {

    private final User user;
    private final PushUpRepository  pushUpRepo;
    private final FriendRepository  friendRepo;
    private final ObservableList<PushUpEntry> recentEntries = FXCollections.observableArrayList();

    public PushUpViewModel() {
        Connection conn = new DatabaseManager().getConnection();
        user       = new UserRepository(conn).findFirst()
                         .orElseThrow(() -> new IllegalStateException("No user found"));
        pushUpRepo = new PushUpRepository(conn);
        friendRepo = new FriendRepository(conn);
        refresh();
    }

    public void logPushUps(int count) {
        pushUpRepo.logPushUps(user.getId(), count, LocalDate.now());
        refresh();
    }

    private void refresh() {
        recentEntries.setAll(pushUpRepo.getRecentEntries(user.getId()));
    }

    // ── Stats ──

    public int getWeeklyTotal()  { return pushUpRepo.getWeeklyTotal(user.getId()); }
    public int getMonthlyTotal() { return pushUpRepo.getMonthlyTotal(user.getId()); }
    public int getOverallTotal() { return pushUpRepo.getOverallTotal(user.getId()); }

    // ── Chart data: one point per day (summed if multiple sessions) ──

    public Map<LocalDate, Integer> getDailyChartData() {
        List<PushUpEntry> all = pushUpRepo.getAllEntries(user.getId());
        Map<LocalDate, Integer> daily = new LinkedHashMap<>();
        for (PushUpEntry e : all) {
            daily.merge(e.date(), e.count(), Integer::sum);
        }
        return daily;
    }

    public ObservableList<PushUpEntry> getRecentEntries() { return recentEntries; }

    // ── Friend competition ──

    public boolean hasFriend() {
        return friendRepo.hasFriend(user.getId());
    }

    /** Locks in the friend choice. Call only once per user. */
    public void selectFriend(String friendName) {
        friendRepo.selectFriend(user.getId(), friendName);
    }

    /**
     * Returns the current friend (simulated up-to-date), or empty if none chosen.
     */
    public Optional<Friend> getFriend() {
        return friendRepo.loadAndSimulate(user.getId());
    }
}
