package com.amlo.shopping.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class Product {
    @Id
    private String uniqId;

    @NotNull
    @Index
    private String pid;

    @NotNull
    private String productName;

    @NotNull
    private float retailPrice;

    @Index
    private String brand;

    @Index
    private String category;

    private String description;
    private String productCategoryTree;
    private String productUrl;
    private String image;
    private String seller;

    @ToMany(referencedJoinProperty = "productId")
    private List<ProductSpecification> productSpecifications;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 694336451)
    private transient ProductDao myDao;

    @Generated(hash = 1617302746)
    public Product(String uniqId, @NotNull String pid, @NotNull String productName, float retailPrice,
            String brand, String category, String description, String productCategoryTree,
            String productUrl, String image, String seller) {
        this.uniqId = uniqId;
        this.pid = pid;
        this.productName = productName;
        this.retailPrice = retailPrice;
        this.brand = brand;
        this.category = category;
        this.description = description;
        this.productCategoryTree = productCategoryTree;
        this.productUrl = productUrl;
        this.image = image;
        this.seller = seller;
    }

    @Generated(hash = 1890278724)
    public Product() {
    }

    public String getUniqId() {
        return this.uniqId;
    }

    public void setUniqId(String uniqId) {
        this.uniqId = uniqId;
    }

    public String getPid() {
        return this.pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getRetailPrice() {
        return this.retailPrice;
    }

    public void setRetailPrice(float retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductCategoryTree() {
        return this.productCategoryTree;
    }

    public void setProductCategoryTree(String productCategoryTree) {
        this.productCategoryTree = productCategoryTree;
    }

    public String getProductUrl() {
        return this.productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1943691747)
    public List<ProductSpecification> getProductSpecifications() {
        if (productSpecifications == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ProductSpecificationDao targetDao = daoSession
                    .getProductSpecificationDao();
            List<ProductSpecification> productSpecificationsNew = targetDao
                    ._queryProduct_ProductSpecifications(uniqId);
            synchronized (this) {
                if (productSpecifications == null) {
                    productSpecifications = productSpecificationsNew;
                }
            }
        }
        return productSpecifications;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 596002778)
    public synchronized void resetProductSpecifications() {
        productSpecifications = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    public String getSeller() {
        return this.seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1171535257)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getProductDao() : null;
    }
    
}
