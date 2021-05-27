Rails.application.routes.draw do
  get '/customers', to: 'customers#index'
  post '/customers', to: 'customers#create'
  put '/customers', to: 'customers#update'
  delete '/customers/:id', to: 'customers#destroy'
end
