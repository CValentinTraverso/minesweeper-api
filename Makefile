.PHONY:

mysql-docker:
	docker-compose up -d db

mysql: mysql-docker
	@echo -n "Waiting for MYSQL: "
	@i=0; while \
	!(mysql -h 127.0.0.1 -P 3306 -uroot -pthisisapw minesweep < mysql-test.sql > error.log); do \
	  sleep 1; echo -n '.'; \
	  if [ $$((i+=1)) -gt 60 ] ; then cat error.log ; exit 1; fi;  \
	done
	@echo "DONE"

test: mysql
	mvn clean test

package: mysql
	mvn clean package

deploy: package
	rm -f ./ansible/files/*
	mkdir -p ./ansible/files
	mv ./target/minesweep-0.0.1.jar ./ansible/files
	ansible-playbook ./ansible/deploy.yml -i ./ansible/aws.inventory