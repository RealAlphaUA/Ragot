package ua.realalpha.ragot.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class PageController {

    private List<ItemStack> itemStacks;
    private Consumer<InventoryClickEvent> event;
    private final RInventory rInventory;
    private final Map<Integer, List<ItemStack>> map;
    private int[] board;
    private int page;

    public PageController(RInventory rInventory) {
        this.rInventory = rInventory;
        this.itemStacks = new ArrayList<>();
        this.map = new HashMap<>();
        this.event = e->{};
        this.page = 0;
    }

    public void setBoard(int slotfrom, int lenth, int width){
        this.board = this.rInventory.getBoard(slotfrom, lenth, width);
    }

    public void setItemStacks(List<ItemStack> itemStacks) {
        this.itemStacks = itemStacks;
    }

    public void setInteractEvent(Consumer<InventoryClickEvent> event){
        this.event = event;
    }

    public void nextPage(){
        if (this.getPage() != getMaxPage()) {
            this.page++;
            this.setPage(this.page);
        }
    }

    public void previousPage(){
        if(this.page > 0) {
            this.page--;
            this.setPage(this.page);
        }
    }

    private void setPage(int page){
        for (int i : this.board) {
            this.rInventory.getMapShare().remove(i);
        }
        List<ItemStack> itemStacks = this.map.get(page);
        for (int i = 0; i < itemStacks.size(); i++) {
            this.rInventory.setItem(this.board[i], itemStacks.get(i), event);
        }
    }

    protected void setUp(){
        int page = 0;
        int size = 1;
        for (ItemStack itemStack : this.itemStacks) {
            if (size == board.length){
                page++;
                size=1;
            }
            if (!this.map.containsKey(page)) this.map.put(page, new ArrayList<>());
            this.map.get(page).add(itemStack);
            size++;
        }
        this.setPage(0);
    }

    public final int getPage() {
        return page+1;
    }

    public final int getMaxPage(){
        return this.map.size();
    }

}