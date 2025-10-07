FROM python:3.11.4
WORKDIR /app

COPY dist/dsservice-1.0.tar.gz .

RUN pip install --no-cache-dir dsservice-1.0.tar.gz

ENV FLASK_APP=src/app/__init__.py

EXPOSE 8093 

CMD [ "flask" , "run", "--host=0.0.0.0" , "--port=8093"]