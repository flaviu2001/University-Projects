class CreateFoods < ActiveRecord::Migration[6.1]
  def change
    create_table :foods do |t|
      t.string :name
      t.string :producer
      t.string :expirationDate

      t.timestamps
    end
  end
end
