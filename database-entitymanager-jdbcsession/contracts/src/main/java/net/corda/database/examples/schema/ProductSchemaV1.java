package net.corda.database.examples.schema;

import net.corda.core.schemas.MappedSchema;
import net.corda.core.serialization.CordaSerializable;

import javax.persistence.*;
import java.util.Arrays;

//schemas will be stored off ledger
public class ProductSchemaV1 extends MappedSchema {

    /**
     * This class must extend the MappedSchema class. Its name is based on the SchemaFamily name
     * and the associated version number abbreviation (V1, V2... Vn).
     * In the constructor, use the super keyword to call the constructor of MappedSchema with
     * the following arguments: a class literal representing the schema family,
     * a version number and a collection of mappedTypes (class literals) which
     * represent JPA entity classes that the ORM layer needs to be configured with for this schema.
     */

    public ProductSchemaV1() {
        super(ProductSchema.class, 1, Arrays.asList(PersistentProduct.class, PersistentProductDetail.class));
    }

    /**
     * The @entity annotation signifies that the specified POJO class' non-transient fields
     * should be persisted to a relational database using the services of an entity manager.
     *
     * The @table annotation specifies properties of the table that will be created to
     * contain the persisted data, in this case we have
     * specified a name argument which will be used the table's title.
     */

    @Entity
    @Table(name = "product")
    public static class PersistentProduct {

        /**
         * The @Column annotations specify the columns that will comprise
         * the inserted table and specify the shape of the fields and associated
         * data types of each database entry.
         */

        @Id @Column(name = "sku") private final Integer sku;
        @Column(name = "name") private final String name;

        /**
         * The @OneToOne annotation specifies a one-to-one relationship.
         *
         * The @JoinColumn and @JoinColumns annotations specify
         * on which columns these tables will be joined on.
         */

        // you could also use many to one, one to many joins
        @OneToOne(cascade = CascadeType.PERSIST)
        @JoinColumns({
                @JoinColumn(name = "detail_id", referencedColumnName = "detail_id"),
        })
        private final PersistentProductDetail productDetail;

        public PersistentProduct(Integer sku, String name, PersistentProductDetail productDetail) {
            this.sku = sku;
            this.name = name;
            this.productDetail = productDetail;
        }

        //Hibernate requires you to add a default constructor by default
        public PersistentProduct() {
            productDetail = null;
            sku = null;
            name = null;
        }

        public Integer getSku() {
            return sku;
        }

        public String getName() {
            return name;
        }

        public PersistentProductDetail getProductDetail() {
            return productDetail;
        }

        @Override
        public String toString() {
            return " sku : " + sku+ " name : " + name + " detail_id : " + productDetail.getDetail_id() + " product_detail : " + productDetail.getDetail();
        }
    }

    @Entity
    @Table(name = "product_detail")
    @CordaSerializable
    public static class PersistentProductDetail {

        @Column(name = "detail") private final String detail;

        // The @Id annotation marks this field as the primary key of the persisted entity.
        @Id
        @Column(name = "detail_id")
        private final Integer detail_id;

        public PersistentProductDetail(String detail, Integer detail_id) {
            this.detail = detail;
            this.detail_id = detail_id;
        }

        public PersistentProductDetail() {
            detail = null;
            detail_id = null;
        }

        public String getDetail() {
            return detail;
        }

        public Integer getDetail_id() {
            return detail_id;
        }
    }

}