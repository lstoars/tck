package com.tickets.ocr.util;

public class CommandExec {
	 /**
     * Timoutʱ�䣬�gλ������
     */
    private long timeout;
    /**
     * ȷ��timeout�ļ��ʱ�䣬�gλ������
     */
    private long interval;
    /**
     *
     * @param timeout timeoutʱ��
     * @param invterval ����timeout�ļ��ʱ��
     */
    public CommandExec(int timeout, int interval){
        this.timeout = timeout;
        this.interval = interval;
    }

    /**
     *
     * ִ���ⲿ����
     * @return Process
     * @throws IllegalThreadStateException
     */
    public Process exec(Process process){
        // �趨timeout
        long limitTime = this.timeout + System.currentTimeMillis();
        // ״̬
        Integer status = null;
        do {
            try {
                status = process.exitValue();
                break;
            } catch (IllegalThreadStateException e) {
            	//�ⲿ�������û�з���ʱ��ϵͳ���������һ��ʱ��
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
     * ��ȡTimoutʱ�䣬�gλ������
     *
     * @return Timoutʱ�䣬�gλ������
     */
    public long getTimeout() {
        return timeout;
    }
    /**
     * ��ȡȷ��timeout�ļ��ʱ�䣬�gλ������
     *
     * @return ȷ��timeout�ļ��ʱ�䣬�gλ������
     */
    public long getInterval() {
        return interval;
    }
}
