package com.tickets;

import java.util.Timer;
import java.util.TimerTask;



public class Reminder {
    //Toolkit toolkit;
    Timer timer;

    public Reminder() {
        timer = new Timer();
        timer.schedule(new RemindTask(),
               0,       
               5*60*1000); 
    }

    class RemindTask extends TimerTask {
        int numWarningBeeps = 3;

        public void run() {
           
        }
    }
    
    public static void main(String[] args) {
		new Reminder();
	}
}

