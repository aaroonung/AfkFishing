package me.aov.task;

import me.aov.AfkFishing;
import me.aov.objects.Chair;

public class HologramUpdaterTask implements Runnable{
    private final AfkFishing main;

    public HologramUpdaterTask(AfkFishing main) {
        this.main = main;
    }

    @Override
    public void run() {
        for(Chair chair : main.getChairManager().getChairsList()){
            chair.count();
        }
    }
}
