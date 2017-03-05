# octolook

FIXME

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server
    
## Debug
    
Connect to remote host with running app:

    ssh -L 7888:localhost:7888 root@do-1
    
Then use your remote nREPL connection to connect

## License

Copyright © 2017 FIXME
