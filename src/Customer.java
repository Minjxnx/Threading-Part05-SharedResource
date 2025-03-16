public class Customer implements Runnable{
    private CoffeeShop shop;
    private int orderID;

    public Customer(CoffeeShop shop, int orderID) {
        super();
        this.shop = shop;
        this.orderID = orderID;
    }



    @Override
    public void run() {
        shop.placeOrder("Order with Order ID : "+orderID);
        System.out.println(Thread.currentThread().getName()+" places "+"Order with Order ID : "+orderID);

    }
}
