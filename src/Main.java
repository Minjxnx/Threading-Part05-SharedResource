public class Main {
    public static void main(String[] args) {
        CoffeeShop shop = new CoffeeShop(5);

        Thread[] customers = new Thread[10];

        for(int i = 0; i < 10; i++) {
            customers[i] = new Thread(new Customer(shop, i+1), "Customer : "+i);
        }

        Thread[] barista = new Thread[3];

        for(int i = 0; i < 3; i++) {
            barista[i] = new Thread(new Barista(shop), "Barista "+i);
            // barista[i].setDaemon(true);
        }

        for (Thread t : customers) {
            t.start();
        }

        for(Thread t : barista) {
            t.start();
        }

        // Wait for all customers to finish placing orders
        for (Thread t : customers) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Signal to baristas that no more orders will be placed
        shop.setShopClosed();

        // Wait for baristas to finish processing all orders
        for (Thread t : barista) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All orders processed. Coffee shop is closed.");

    }
}