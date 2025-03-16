import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Shared resource
// Monitor

public class CoffeeShop {

    // shared variable
    private Queue<String> queue = new LinkedList<String>();
    private int maxSize;
    private Lock lock = new ReentrantLock(true);
    // if queue is full then Customers will wait on queueFull condition
    private Condition queueFull = lock.newCondition();
    // if the queue if empty Baristas will wait on queueEmpty condition
    private Condition queueEmpty = lock.newCondition();
    private boolean shopClosed = false;
    public CoffeeShop(int size) {
        super();
        this.maxSize = size;
    }

    // Producer - Customer of the coffee shop will call this method
    public void placeOrder(String order) {
        try {
            lock.lock();
            // if queue.size() == maxSize is true the meaning is queue is full
            while (queue.size() == maxSize) {
                try {
                    queueFull.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            queue.offer(order);
            queueEmpty.signalAll();

        }finally {
            lock.unlock();
        }
    }

    // Consumer - Barista of the coffee shop will call this method
    public String prepareOrder() {
        try {
            lock.lock();
            // if queue is empty and shop is closed, return null to signal barista to terminate
            if(queue.size() == 0 && shopClosed) {
                return null;
            }
            // if queue.size() == 0 is true meaning queue is empty
            // barista has to wait until an order arrives
            while(queue.size() == 0) {
                try {
                    queueEmpty.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            String order = queue.poll();
            queueFull.signalAll();
            return order;
        }finally {
            lock.unlock();
        }
    }

    public int getSize() {
        try {
            lock.lock();
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
    public void setShopClosed() {
        try {
            lock.lock();
            this.shopClosed = true;
            // Wake up all baristas so they can check if they should terminate
            queueEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public boolean isShopClosed() {
        try {
            lock.lock();
            return shopClosed;
        } finally {
            lock.unlock();
        }
    }
}
