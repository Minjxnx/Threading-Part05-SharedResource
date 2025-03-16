public class Barista implements Runnable {
    private CoffeeShop shop;

    public Barista(CoffeeShop shop) {
        super();
        this.shop = shop;
    }

    @Override
    public void run() {
        while(!shop.isShopClosed() || shop.getSize() > 0) {
            String order = shop.prepareOrder();
            if(order != null) {
                System.out.println(Thread.currentThread().getName()+" prepares "+order);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(Thread.currentThread().getName() + " is going home.");
    }
}
