package net.heyzeer0.mgh.mixins.bibliocraft;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Created by HeyZeer0 on 15/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "jds/bibliocraft/blocks/ContainerFancyWorkbench", remap = false)
public abstract class MixinContainerFancyWorkbench {

    @Shadow
    private InventoryPlayer playerInventory;

    @Shadow
    public InventoryCrafting playerCraftMatrix;

    @Shadow
    private int[] ingredientCounts;

    @Shadow
    private String[] ingredientNames;

    @Overwrite
    public void loadPlayerInventorytoRecipeBookGrid(ItemStack[] grid, int id) {
        if (this.playerInventory.player.getEntityId() != id)
        {
            return;
        }

        ItemStack slotStack = null;
        ItemStack invStack = null;
        ItemStack matrixStack = null;

        int[] matrixSizes = new int[9];
        ItemStack[] newMatrix = new ItemStack[9];

        int newStackCount = 0;

        compareingredients(grid);

        for (int n = 0; n < 9; n++)
        {
            slotStack = grid[n];
            if (slotStack != null)
            {
                int stackcount = 0;
                for (int m = 0; m < this.playerInventory.mainInventory.length; m++)
                {
                    invStack = this.playerInventory.mainInventory[m];
                    if (invStack != null)
                    {
                        if (slotStack.getUnlocalizedName().matches(invStack.getUnlocalizedName()) && slotStack.isStackable())
                        {
                            stackcount += invStack.stackSize;
                        }
                    }
                }
                for (int m = 0; m < 9; m++)
                {
                    if (slotStack.getUnlocalizedName().matches(this.ingredientNames[m]))
                    {
                        int totalStackCount = stackcount;
                        stackcount /= this.ingredientCounts[m];
                        if (stackcount <= 0)
                            break;
                        if (stackcount > 64)
                        {
                            stackcount = 64;
                        }
                        matrixStack = this.playerCraftMatrix.getStackInSlot(n);

                        newStackCount = stackcount;
                        if (matrixStack != null)
                        {
                            matrixSizes[n] = matrixStack.stackSize;
                            if (matrixStack.getUnlocalizedName().matches(slotStack.getUnlocalizedName()) && slotStack.isStackable())
                            {
                                if (matrixSizes[n] + stackcount >= 64)
                                {
                                    stackcount = 64;
                                    newStackCount = 64 - matrixSizes[n];
                                }
                                else
                                {
                                    stackcount += matrixSizes[n];
                                }
                            }
                            else
                            {
                                stackcount = 0;
                                matrixSizes[n] = 0;
                            }
                        }
                        if (stackcount == 0)
                            break;
                        slotStack.stackSize = stackcount;
                        this.playerCraftMatrix.setInventorySlotContents(n, slotStack);
                        ItemStack stackcopy = slotStack.copy();
                        stackcopy.stackSize = newStackCount;
                        newMatrix[n] = stackcopy;
                        break;
                    }
                }
            }
        }

        slotStack = null;
        invStack = null;
        for (int n = 0; n < 9; n++)
        {
            slotStack = newMatrix[n];
            if (slotStack != null)
            {
                int stackcount = slotStack.stackSize;
                if (stackcount > 0)
                {
                    for (int m = 0; m < this.playerInventory.mainInventory.length; m++)
                    {
                        invStack = this.playerInventory.mainInventory[m];
                        if (invStack != null)
                        {
                            if (invStack.getUnlocalizedName().matches(slotStack.getUnlocalizedName()) && slotStack.isStackable())
                            {
                                if (invStack.stackSize > stackcount)
                                {
                                    this.playerInventory.mainInventory[m].stackSize -= stackcount;
                                    stackcount = 0;
                                    break;
                                }
                                if (invStack.stackSize == stackcount)
                                {
                                    this.playerInventory.mainInventory[m] = null;
                                    stackcount = 0;
                                    break;
                                }

                                stackcount -= this.playerInventory.mainInventory[m].stackSize;
                                this.playerInventory.mainInventory[m] = null;
                            }
                        }
                    }
                }
            }
        }

    }

    @Shadow
    public void compareingredients(ItemStack[] stacks) {}

}