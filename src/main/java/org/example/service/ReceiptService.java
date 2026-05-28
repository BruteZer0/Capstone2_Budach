package org.example.service;

import org.example.data.Order;
import org.example.util.FileManager;

public class ReceiptService {
    private static final String RECEIPTS_DIR = "src/main/resources/receipts";

    public ReceiptService() {
        FileManager.ensureDirectory(RECEIPTS_DIR);
    }

    public String saveReceipt(Order order) {
        String filePath = RECEIPTS_DIR + "/" + order.getReceiptFileName();
        boolean saved   = FileManager.writeFile(filePath, order.toReceiptString());
        return saved ? filePath : null;
    }
}
