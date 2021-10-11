class CustomersController < ApplicationController
  def index
    @customers = Customer.all
    render json: @customers
  end

  def show
    @customer = Customer.find(params[:id])
    render json: @customer
  end

  def create
    @customer = Customer.create(
      name: params[:name],
      phoneNumber: params[:phoneNumber]
    )
    @customer.save
    render json: @customer, status: :ok
  end

  def update
    @customer = Customer.find(params[:id])
    @customer.update(
      name: params[:name],
      phoneNumber: params[:phoneNumber]
    )
  end

  def destroy
    @customer = Customer.find(params[:id])
    @customer.destroy
  end
end
