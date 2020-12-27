import java.util.concurrent.locks.ReentrantLock;

/**
 * 银行有一个账户，有两个储户分别向同一个账户存3000元，每次存1000存3次，每次存完打印余额
 * 分析：
 * 1.是否是多线程？是，两个储户线程，创建线程
 * 2.是否有共享数据？有，账户
 * 3.是否有线程安全问题？有
 * 4.如何解决线程安全问题？同步机制，有三种方式。
 * @author ranan
 * @create 2020-12-27 20:35
 */
class MyThread implements Runnable{
    private Account acct = new Account(0);
    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            acct.deposit(1000);
        }
    }
}
class Account{
    private double balance;
    private ReentrantLock Lock = new ReentrantLock();
    public Account(double balance)      {
        this.balance = balance;
    }
    //存钱
    public void deposit(double amt){
        if(amt>0){
            try {
                Lock.lock();
                balance += amt;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"：存钱成功，余额为" + balance);
            } finally {
                Lock.unlock();
            }
        }
    }

}
public class AccountTest {
    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        //线程1
        Thread t1 = new Thread(myThread);
        //线程2
        Thread t2 = new Thread(myThread);
        t1.setName("甲");
        t2.setName("乙");
        t1.start();
        t2.start();
    }
}
