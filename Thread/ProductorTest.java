/**
 * 线程通信的应用：生产者/消费者问题。
 * 生产者(productor)将产品交给店员(clerk),消费者(customer)从店员处取走产品，店员一次只能持有固定数量的产品(比如20)，如果生产者试图生产更多的产品，店员会叫停，如果店中有空位放产品了再通知生产者继续生成，如果店中没有产品，会告诉消费者等一下，如果店中有产品了再通知消费者来取走产品
 * 分析：
 * 1.是否是多线程：是，生产者与消费者
 * 2.是否有共享数据：是，共享数据是产品
 * 3.如何解决线程的安全问题？同步机制，有三种方法
 * 4.线程通信
 * @author ranan
 * @create 2020-12-28 15:31
 */
class Clerk{

    private int productCount = 0;
    public synchronized void produceProduct() throws InterruptedException {
        if (productCount < 20) {
            System.out.println(Thread.currentThread().getName() + ":开始生成第" + ++productCount + "个产品");
            //生产完就可以唤醒
            notify();
        } else {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    public synchronized void consumeProduct() {
        if(productCount>0){
            System.out.println(Thread.currentThread().getName() + ":开始消费第"+  productCount-- + "个产品");
            notify();
        }else{
            //等待
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
class Producer implements Runnable{
    private Clerk clerk;
    public Producer(Clerk clerk){
        this.clerk = clerk;
    }
    //生产者
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "开始生产产品");
        while (true){
            try {
                clerk.produceProduct();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
class Consumer implements  Runnable{
    //消费哲
    private Clerk clerk;
    public Consumer(Clerk clerk){
        this.clerk = clerk;
    }
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "开始消费产品");
        while (true){
            clerk.consumeProduct();
        }
    }
}
public class ProductorTest {
    public static void main(String[] args) {
        Clerk clerk = new Clerk();
        Thread p1 = new Thread(new Producer(clerk));
        p1.setName("生产者1");
        Thread c1 =new Thread(new Consumer(clerk));
        c1.setName("消费者1");
        p1.start();
        c1.start();
    }
}
