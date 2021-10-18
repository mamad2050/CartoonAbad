package ir.andromeda.cartoonabad.data

import ir.cafebazaar.poolakey.entity.PurchaseInfo

object PurchaseContainer {
    var purchaseInfo: PurchaseInfo? = null
        private set

    fun setPurchaseInfo(purchaseInfo: PurchaseInfo) {
        PurchaseContainer.purchaseInfo = purchaseInfo
    }
}