require 'ubygems'
require 'ruby_parser.rb'
require 'pp'
require File.expand_path('../structure.rb',__FILE__)
require File.expand_path('../generation.rb',__FILE__)

def assert
  result=yield
  #pp "RESULT:",result
  unless result
    pp "Assertion failed"
    raise 1
  end
end

class IO
  def self.load(filename)
    File.open(filename) {|f|f.read}
  end
end

class String
  def indent
    "  "+gsub("\n","\n  ")
  end

  def upcaseFirstLetter
    self[0..0].upcase+self[1..-1]
  end
end

class Compiler

  Result=Struct.new(:before,:within,:after)
  def initialize()
    @counter=0
  end

  def tmpVar
    @counter+=1
    "tmp_#{@counter}"
  end

  def curModule
    "root"
  end

  def compile(info,args,filename)
    assert{filename}
    return nil if args.nil?
    parent=self
    parent=info.parent if info
    info=Base::Info.new(parent,filename,args.line,self)
    parent||=self
    klassName=args[0].to_s.upcaseFirstLetter

    klass=eval(klassName+"Code")
    cargs=args[1..-1]
    begin
      obj=klass.new(info,*cargs)
      obj.init
    rescue Object=>e
      pp "#{klass}.new called with (#{parent},#{cargs.inspect})"
      pp e,e.backtrace
      #raise e
      exit
    end
    obj
  end
end

#'pp self.methods #.instance_variables
#pp __FILE__

if $0 == __FILE__


  @processor = RubyParser.new
  c=STDIN.read

  a=@processor.parse(c)
  c=Compiler.new
  #pp a
  result=c.compile(nil,a,ARGV[0]||"filename.rb")

  puts "include VMIO"
  puts result.code
end