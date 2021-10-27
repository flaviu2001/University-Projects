class FoodsController < ApplicationController
  def index
    @foods = Food.all
    render json: @foods
  end

  def show
    @food = Food.find(params[:id])
    render json: @food
  end

  def create
    @food = Food.create(
      name: params[:name],
      producer: params[:producer],
      expirationDate: params[:expirationDate]
    )
    @food.save
    render json: @food, status: :ok
  end

  def update
    @food = Food.find(params[:id])
    @food.update(
      name: params[:name],
      producer: params[:producer],
      expirationDate: params[:expirationDate]
    )
  end

  def destroy
    @food = Food.find(params[:id])
    @food.destroy
  end
end
