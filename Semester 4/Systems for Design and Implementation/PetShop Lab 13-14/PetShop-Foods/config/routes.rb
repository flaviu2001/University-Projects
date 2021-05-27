Rails.application.routes.draw do
  get '/foods', to: 'foods#index'
  post '/foods', to: 'foods#create'
  put '/foods', to: 'foods#update'
  delete '/foods/:id', to: 'foods#destroy'
end
