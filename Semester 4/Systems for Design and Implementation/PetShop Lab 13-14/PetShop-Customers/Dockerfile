FROM ruby:3.0.1

WORKDIR /usr/src/app
COPY . .

RUN gem install bundler:2.2.17
RUN bundle update activesupport rails-html-sanitizer
RUN bundle install

CMD ["bash", "-c", "rails db:migrate && rails server -b 0.0.0.0 -p 3000"]
