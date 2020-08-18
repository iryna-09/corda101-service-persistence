package net.corda.database.examples.schema;

import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.serialization.CordaSerializable;

import javax.persistence.*;
import java.util.Arrays;

//schemas will be stored off ledger
public class ProductSchemaV1 extends MappedSchema {
    public ProductSchemaV1() {
        super(ProductSchema.class, 1, Arrays.asList(PersistentProduct.class));
    }

    // add the entity and table annotations
    @Entity
    @Table(name = "product")
    public static class PersistentProduct extends PersistentState {
        @Column(name = "sku") private final Integer sku;
        @Column(name = "name") private final String name;

        public PersistentProduct(Integer sku, String name) {
            this.sku = sku;
            this.name = name;
        }

        //Hibernate requires you to add a default constructor by default
        public PersistentProduct() {
            sku = null;
            name = null;
        }

        public Integer getSku() {
            return sku;
        }

        public String getName() {
            return name;
        }


        @Override
        public String toString() {
            return "sku : " + sku+ "name : " + name;
        }
    }

}