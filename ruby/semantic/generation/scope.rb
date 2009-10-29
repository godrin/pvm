class ScopeCode
  def init
  end
  def code
    if @body
      @body.code
    else
      []
    end
  end
end