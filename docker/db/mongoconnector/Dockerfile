from python
run pip install --upgrade pip
run pip install elasticsearch
run pip install mongo-connector
run pip install 'mongo-connector[elastic5]'
run pip list|grep elastic
entrypoint mongo-connector -m mongo:27017 -t es:9200 -d elastic2_doc_manager --admin-username mongo_user --password mongo_password && tail -f /mongo-connector.log
