package com.tickets.ocr.util;

public class CommandExec {
	 /**
     * Timout时间，単位：毫秒
     */
    private long timeout;
    /**
     * 确认timeout的间隔时间，単位：毫秒
     */
    private long interval;
    /**
     *
     * @param timeout timeout时间
     * @param invterval 换算timeout的间隔时间
     */
    public CommandExec(int timeout, int interval){
        this.timeout = timeout;
        this.interval = interval;
    }

    /**
     *
     * 执行外部命令
     * @return Process
     * @throws IllegalThreadStateException
     */
    public Process exec(Process process){
        // 设定timeout
        long limitTime = this.timeout + System.currentTimeMillis();
        // 状态
        Integer status = null;
        do {
            try {
                status = process.exitValue();
                break;
            } catch (IllegalThreadStateException e) {
            	//外部命令调用没有返回时，系统将向后推移一段时间
                try {
                    Thread.sleep(this.interval);
                } catch (InterruptedException we) {
                    return null;
                }
            }
        } while (System.currentTimeMillis() < limitTime);
        if (status == null) {
        	return null;
        }
        return process;
    }

    /**
     * 提取Timout时间，単位：毫秒
     *
     * @return Timout时间，単位：毫秒
     */
    public long getTimeout() {
        return timeout;
    }
    /**
     * 提取确认timeout的间隔时间，単位：毫秒
     *
     * @return 确认timeout的间隔时间，単位：毫秒
     */
    public long getInterval() {
        return interval;
    }
}
